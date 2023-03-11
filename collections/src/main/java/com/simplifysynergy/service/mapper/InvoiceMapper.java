package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.Invoice;
import com.simplifysynergy.domain.InvoicePayment;
import com.simplifysynergy.domain.UserSubscription;
import com.simplifysynergy.service.dto.InvoiceDTO;
import com.simplifysynergy.service.dto.InvoicePaymentDTO;
import com.simplifysynergy.service.dto.UserSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    @Mapping(target = "invoicePayment", source = "invoicePayment", qualifiedByName = "invoicePaymentId")
    @Mapping(target = "userSubscription", source = "userSubscription", qualifiedByName = "userSubscriptionId")
    InvoiceDTO toDto(Invoice s);

    @Named("invoicePaymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InvoicePaymentDTO toDtoInvoicePaymentId(InvoicePayment invoicePayment);

    @Named("userSubscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserSubscriptionDTO toDtoUserSubscriptionId(UserSubscription userSubscription);
}
