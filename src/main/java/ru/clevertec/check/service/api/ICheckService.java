package ru.clevertec.check.service.api;

import ru.clevertec.check.core.dto.Check;
import ru.clevertec.check.core.dto.DebitCard;
import ru.clevertec.check.core.dto.ProductRequest;

import java.util.List;

public interface ICheckService {
    Check createCheck(List<ProductRequest> productRequestList, int discountCardNumber, DebitCard debitCard);

    String checkToText(Check check);
}
