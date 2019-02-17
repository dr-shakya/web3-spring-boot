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

    public final static String PRIVATE_KEY = "b9ebb18959989080e60ba98775da10a21aad3a431bb5c0b0e3ba2cb388947488";

    public final static String CONTRACT_ADDRESS = "0xA39E7BD9C9fD30EA96e90Ae11f6fE8b5e0e4e26A";

    public static void main(String[] args) {

        try {
            new Main();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Main() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:7545"));
        printWeb3Version(web3j);

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

        //uncomment for deploying the contract
//        String contractAddress = deployContract(web3j, credentials, contractGasProvider);
//        System.out.println("-------------------Contract deployed at address: "+contractAddress);

        Greeting contract = loadContract(web3j, credentials, contractGasProvider);
        System.out.println("-------------------Contract loaded from address: "+contract.getContractAddress());
        TransactionReceipt transactionReceipt = contract.setGreeting("Hello again").send();
        System.out.println("-------------------Transaction Receipt:" + transactionReceipt);

        String newValue= contract.greet().send();
        System.out.println("-------------------New Value: "+newValue);

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
        System.out.println("-------------------Web3 client version: " + web3ClientVersionString);
    }

    public Credentials getCredentialsFromPrivateKey() {
        return Credentials.create(PRIVATE_KEY);
    }

    //deploy contract
    private String deployContract(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) throws Exception {
        System.out.println("-------------------Deploying Contract");
        return Greeting.deploy(web3j, credentials, contractGasProvider, "Hello Blockchain World").send().getContractAddress();
    }

    //load contract
    private Greeting loadContract(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider){
        System.out.println("-------------------Loading Contract");
        return Greeting.load(CONTRACT_ADDRESS, web3j, credentials, contractGasProvider);
    }
}
