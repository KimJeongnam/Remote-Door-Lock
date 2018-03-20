#!/usr/bin/env python
import sys, time
from BLEDEVICE import scanble, BLEDevice

if len(sys.argv) != 2 and len(sys.argv)!=3:
    print("Usage: python blecomm.py <ble address>")
    print("Scan devices are as follows:")
    print(scanble(timeout=3))
    sys.exit(1)

hm10 = BLEDevice(sys.argv[1])

def test_sendrecv():
    while True:
        vh=hm10.getvaluehandle("ffe1")
        hm10.writecmd(vh, "test\r\n")
        data = hm10.notify()
        if data is not None:
            print("Received: ", data)
        time.sleep(1)

def Send_BLE():
    message = sys.argv[2]+"\n"
    vh=hm10.getvaluehandle("ffe1")
    hm10.writecmd(vh, message.encode("ascii"))
    print ("Done")

if len(sys.argv) == 3:
    Send_BLE()
else:
    test_sendrecv()
