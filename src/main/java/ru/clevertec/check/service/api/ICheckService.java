package ru.clevertec.check.service.api;

import ru.clevertec.check.core.dto.Check;

public interface ICheckService {
    Check createCheck(String[] args);

    String checkToText(Check check);
}
