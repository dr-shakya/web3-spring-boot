pragma solidity ^0.5.1;

contract Greeting{
    address creator;
    string message;

    constructor(string memory _message) public{
        creator = msg.sender;
        message = _message;
    }

    function greet() public view returns (string memory) {
        return message;
    }

    function setGreeting(string memory _message) public {
        message = _message;
    }
}