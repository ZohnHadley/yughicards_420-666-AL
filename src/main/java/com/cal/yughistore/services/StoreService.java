package com.cal.yughistore.services;

import com.cal.yughistore.model.yughiocard.enums.EnumCardStockStatus;
import com.cal.yughistore.services.dto.applicationuser.ClientUserDTO;
import com.cal.yughistore.services.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.services.user.AdminUserService;
import com.cal.yughistore.services.user.ClientUserService;
import com.cal.yughistore.services.yughiocard.YughioCardService;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
    private final YughioCardService yughioCardService;
    private final AdminUserService adminUserService;
    private final ClientUserService clientUserService;

    public StoreService(YughioCardService yughioCardService, AdminUserService adminUserService, ClientUserService clientUserService) {
        this.yughioCardService = yughioCardService;
        this.adminUserService = adminUserService;
        this.clientUserService = clientUserService;
    }

    public YughioCardDTO addCard(YughioCardDTO cardDTO){
        if(yughioCardService.checkIfExist(cardDTO) == true){
            cardDTO.setQuantity_in_stock(cardDTO.getQuantity_in_stock() + 1);
            cardDTO.setStock_status(EnumCardStockStatus.IN_STOCK);
        }
        return yughioCardService.save(cardDTO);
    }

    public YughioCardDTO addCard(YughioCardDTO cardDTO, int quantity){
        if(yughioCardService.checkIfExist(cardDTO) == true){
            cardDTO.setQuantity_in_stock(cardDTO.getQuantity_in_stock() + quantity);
            cardDTO.setStock_status(EnumCardStockStatus.IN_STOCK);
        }
        return yughioCardService.save(cardDTO);
    }

}
