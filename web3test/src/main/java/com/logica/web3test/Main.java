package com.logica.web3test;

import com.logica.web3test.contracts.Greeting;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;

import java.io.IOException;
import java.math.BigInteger;

public class Main {

    public final static String PRIVATE_KEY = "9148c951eb2d67af33172687613843d781c10e5b4101055aa523ef60cb2203f6";

    public final static String CONTRACT_ADDRESS = "0xBe216D325406cE734859a2af04B968e740e81056";

    public static void main(String[] args) {

        try {
            new Main();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Web3j web3j = Web3j.build(new HttpService());
        printWeb3Version(web3j);
        Credentials credentials = get*/

    }

    private Main() throws Exception {
        Web3j web3j = Web3j.build(new HttpService());

        Credentials credentials = getCredentialsFromPrivateKey();
        /*TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
        Transfer transfer = new Transfer(web3j, transactionManager);*/

        ContractGasProvider contractGasProvider = new ContractGasProvider() {
            @Override
            public BigInteger getGasPrice(String contractFunc) {
                return BigInteger.valueOf(2000000000);
            }

            @Override
            public BigInteger getGasPrice() {
                return null;
            }

            @Override
            public BigInteger getGasLimit(String contractFunc) {
                return BigInteger.valueOf(6721975);
            }

            @Override
            public BigInteger getGasLimit() {
                return null;
            }
        };

        Greeting contract = loadContract(web3j, credentials, contractGasProvider);
        System.out.println("-------------------Contract Address: "+contract.getContractAddress());
        TransactionReceipt transactionReceipt = contract.setGreeting("Hello again").send();
        System.out.println("--------------------------Transactoin Receipt:" + transactionReceipt);

        String newValue= ((Greeting) contract).greet().send();
        System.out.println("----------------------------New Value: "+newValue);

//        String contractAddress = contract.getContractAddress();

//        TransactionReceipt transactionReceipt = contract

    }

    //print web3 version
    public void printWeb3Version(Web3j web3j) {
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3j.web3ClientVersion().send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String web3ClientVersionString = web3ClientVersion.getWeb3ClientVersion();
        System.out.println("Web3 client version: " + web3ClientVersionString);
    }

    public Credentials getCredentialsFromPrivateKey() {
        return Credentials.create(PRIVATE_KEY);
    }

    //deploy contract
    private Greeting deployContract(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) throws Exception {
        return Greeting.deploy(web3j, credentials, contractGasProvider, "Hello Blockchain World").send();
    }

    //load contract
    private Greeting loadContract(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider){
        return Greeting.load(CONTRACT_ADDRESS, web3j, credentials, contractGasProvider);
    }
}
