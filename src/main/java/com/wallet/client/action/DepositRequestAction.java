package com.wallet.client.action;

import com.wallet.service.Deposit;
import com.wallet.service.DepositServiceGrpc;
import io.grpc.ManagedChannel;

public class DepositRequestAction implements RequestAction{
    private Long userId;
    private String amount;
    private String currencyCode;
    private ManagedChannel channel;

    public DepositRequestAction(Long userId, String amount, String currencyCode) {
        this.userId = userId;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public DepositRequestAction(ManagedChannel channel, Long userId, String amount, String currencyCode) {
        this.channel = channel;
        this.userId = userId;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public void doAction(ManagedChannel channel) {
        Deposit.DepositFundsRequest depositRequest = Deposit.DepositFundsRequest.newBuilder()
                .setUserId(userId)
                .setAmount(amount)
                .setCurrency(currencyCode)
                .build();
        System.out.println("deposit action "+ currencyCode);
        DepositServiceGrpc.newBlockingStub(channel).depositFunds(depositRequest);
    }
}
