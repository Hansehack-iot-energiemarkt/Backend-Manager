# coding=utf-8
from iota import *
from flask import Flask
from flask import request
from flask import jsonify
from flask import abort
import time
import json
import sys

app = Flask(__name__)

SEED1 = b"THESEEDOFTHEWALLETSENDINGGOESHERE999999999999999999999999999999999999999999999999"
TAG = b'HANSEHACKTESTA'

# Create the API instance.
api =\
  Iota(
    # URI of a locally running node.
    'https://potato.iotasalad.org:14265',

    # Seed used for cryptographic functions.
    seed = SEED1
  )

@app.route('/sell/', methods=['POST']) #GET requests will be blocked
def sell():
    print(request.get_json())
    address = api.get_new_addresses(index=0, count=None, security_level=1)['addresses'][0]
    send_transaction(address.as_json_compatible()['trytes'], "{}".format(request.get_json()))
    print({"address":address.as_json_compatible()['trytes']})
    return jsonify({"address":address.as_json_compatible()['trytes']})


@app.route('/sell/', methods=['GET'])
def checkoffers():
    print(request.get_json())
    offers = get_offers()
    print(offers)
    return jsonify(offers)


@app.route('/buy/<buy_address>/', methods=['POST']) #GET requests will be blocked
def buy(buy_address):

    address = Address(buy_address)
    print(request.get_json())
    send_transaction(address.as_json_compatible()['trytes'], "{}".format(request.get_json()))
    print({"address":address.as_json_compatible()['trytes']})
    return jsonify({"address":address.as_json_compatible()['trytes']})

@app.route('/sell/<address>', methods=['GET'])
def checkforsale(address):
    print(request.get_json())
    buys = get_buys(address)
    print(buys)
    return jsonify(buys)

@app.route('/confirm/<confirm_address>', methods=['POST']) #GET requests will be blocked
def confirm(confirm_address):
    address = Address(confirm_address)
    print(request.get_json())
    send_transaction(address.as_json_compatible()['trytes'], "{}".format(request.get_json()))
    print({"address":address.as_json_compatible()['trytes']})
    return jsonify({"address":address.as_json_compatible()['trytes']})

@app.route('/confirm/<address>', methods=['GET'])
def waitforconfirm(address):
    print(request.get_json())
    confirm = check_confirm(address)
    print(confirm)
    if confirm.count() == 0:
        abort(404)
    return jsonify(confirm)

def send_transaction(recipient, message):
  # For more information, see :py:meth:`Iota.send_transfer`.
  api.send_transfer(
    depth=8,

    # One or more :py:class:`ProposedTransaction` objects to add to the
    # bundle.
    transfers=[
      ProposedTransaction(
        # Recipient of the transfer.
        address=Address(
            recipient,
        ),

        # Amount of IOTA to transfer.
        # This value may be zero.
        value=0,

        # Optional tag to attach to the transfer.
        tag=Tag(TAG),

        # Optional message to include with the transfer.
        message=TryteString.from_string(message),
      ),
    ],
  )

def get_offers():
    transactionHashes = api.find_transactions(tags=[TAG])
    print(transactionHashes)


    transactionHash = transactionHashes['hashes']
    trytes:TryteString = api.get_trytes(transactionHash)['trytes']
    epoch_time = int(time.time())
    transactions = []
    for tryte in trytes:
        transaction = Transaction.from_tryte_string(tryte)
        try:
            json_text = transaction.signature_message_fragment.as_string().replace("'", '"')
            print(json_text)
            message = json.loads(json_text)
            if epoch_time - transaction.timestamp < 10*60*60 and message['type'] == "sell":
                transactions.append({'address':transaction.address.as_json_compatible()['trytes'], 'message':message})
        except AttributeError as e:
            print(e)
        except json.decoder.JSONDecodeError as e:
            print(e)
        except:
            print("Unexpected error:", sys.exc_info()[0])
            pass
    return transactions

def get_buys(address):
    transactionHash = get_transactions_by_address(address)['hashes']
    trytes:TryteString = api.get_trytes(transactionHash)['trytes']
    epoch_time = int(time.time())
    transactions = []
    for tryte in trytes:
        transaction = Transaction.from_tryte_string(tryte)
        try:
            message = json.loads(transaction.signature_message_fragment.as_string().replace("'", '"'))
            if epoch_time - transaction.timestamp < 10*60*60 and message['message']['type'] == "buy":
                transactions.append({'address':transaction.address.as_json_compatible()['trytes'], 'message':message['message']})
        except KeyError as e:
            print(e)
        except:
            print("Unexpected error:", sys.exc_info()[0])
            pass
    return transactions

def check_confirm(address):
    transactionHash = get_transactions_by_address(address)['hashes']
    trytes:TryteString = api.get_trytes(transactionHash)['trytes']
    epoch_time = int(time.time())
    transactions = []
    for tryte in trytes:
        transaction = Transaction.from_tryte_string(tryte)

        try:
            message = json.loads(transaction.signature_message_fragment.as_string().replace("'", '"'))
            if epoch_time - transaction.timestamp < 10*60*60 and message['message']['type'] == "confirm":
                transactions.append({'address':transaction.address.as_json_compatible()['trytes'], 'message':message['message']})
        except:
            print("Unexpected error:", sys.exc_info()[0])
            pass
    return transactions


def get_transactions_by_address(address):
   return api.find_transactions(addresses=[address], tags=[TAG])

