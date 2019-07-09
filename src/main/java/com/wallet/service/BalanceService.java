package com.wallet.service;

import com.google.rpc.Status;
import com.wallet.domain.Currency;
import com.wallet.repository.WalletRepository;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@GRpcService
public class BalanceService extends BalanceServiceGrpc.BalanceServiceImplBase {

    @Autowired
    WalletRepository walletRepository;

    public void getBalance(Balance.GetBalanceRequest request,
                           StreamObserver<Balance.GetBalanceResponse> responseObserver) {
        Map<Currency, BigDecimal> walletsInfo = walletRepository.getBalance(request.getUserId());

        try {
            responseObserver.onNext(createGetBalanceResponse(walletsInfo));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(StatusProto.toStatusRuntimeException(
                    Status.newBuilder().setCode(13).setMessage(e.getMessage()).build()));
        }
    }

    private Balance.GetBalanceResponse createGetBalanceResponse(Map<Currency, BigDecimal> walletsInfo) {
        Map<String, String> newMap =
                walletsInfo.entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> e.getKey().toString(),
                                e -> e.getValue().toString()
                        ));
        Balance.GetBalanceResponse response = Balance.GetBalanceResponse.newBuilder().putAllBalance(newMap).build();
        return response;
    }


}
