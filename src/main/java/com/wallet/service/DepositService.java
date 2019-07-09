package com.wallet.service;

import com.google.rpc.Status;
import com.wallet.domain.Currency;
import com.wallet.repository.WalletRepository;
import com.wallet.util.CurrencyUtil;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import org.apache.log4j.Logger;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@GRpcService
public class DepositService extends DepositServiceGrpc.DepositServiceImplBase {
    final static Logger LOGGER = Logger.getLogger(DepositService.class);

    @Autowired
    WalletRepository walletRepository;

    @Override
    public void depositFunds(Deposit.DepositFundsRequest request,
                             StreamObserver<Deposit.DepositFundsResponse> responseObserver) {
        try {
            CurrencyUtil.checkCurrency(request.getCurrency());
            walletRepository.depositFunds(request.getUserId(), new BigDecimal(request.getAmount()),
                    Currency.valueOf(request.getCurrency()));

            Deposit.DepositFundsResponse response = Deposit.DepositFundsResponse.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            LOGGER.info("deposit service finished");
        } catch (Exception e) {
            LOGGER.info("deposit service error");
            responseObserver.onError(StatusProto.toStatusRuntimeException(
                    Status.newBuilder().setCode(13).setMessage(e.getMessage()).build()));
        }
    }

}
