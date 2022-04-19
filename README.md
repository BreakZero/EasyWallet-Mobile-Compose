[![Checking Build](https://github.com/BreakZero/EasyWallet-Mobile-Compose/actions/workflows/check.yml/badge.svg?branch=main)](https://github.com/BreakZero/EasyWallet-Mobile-Compose/actions/workflows/check.yml)

# EasyWallet

## Setup
1. make a new folder into the root project, add config properties and keystore.
2. add your config keys and value about EtherScan/Infura or any other data source apikey you need.
3. check `keyStoreProperties()` and `BaseExtension.moduleConfig()` from composing build module dep-version or setup gradle config that you like.

## Noted
if you want to run-up the project, you need 2 api keys, one from infura, and one from etherscan.io
insert the content format as below into file `configs.properties` in the keystore folder.
more detail please check the gradle script.
```properties
apikey.infura="a9db************2ad2ee7"
apikey.etherscan="XWNI**********YP4F6"
```
