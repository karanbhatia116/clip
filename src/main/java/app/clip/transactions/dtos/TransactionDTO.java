package app.clip.transactions.dtos;

import app.clip.commons.money.Money;

public record TransactionDTO(
        Long id,
        Long userId,
        Long friendId,
        Money splitAmount,
        Long associatedGroupId
) {
}
