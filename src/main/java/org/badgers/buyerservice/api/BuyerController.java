package org.badgers.buyerservice.api;

import lombok.RequiredArgsConstructor;
import org.badgers.buyerservice.dto.BuyerRequestDto;
import org.badgers.buyerservice.dto.BuyerResponseDto;
import org.badgers.buyerservice.dto.filter.BuyerFilter;
import org.badgers.buyerservice.service.BuyerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/buyers")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;

    @GetMapping("/{id}")
    public ResponseEntity<BuyerResponseDto> getBuyer(@PathVariable UUID id) {
        BuyerResponseDto buyer = buyerService.findById(id);
        return ResponseEntity.ok(buyer);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BuyerResponseDto> updateBuyer(@PathVariable UUID id, @RequestBody BuyerRequestDto buyer) {
        BuyerResponseDto responseDto = buyerService.updateById(id, buyer);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/self")
    public ResponseEntity<BuyerResponseDto> getSelf(@RequestHeader("X-Auth-User-ID") UUID id) {
        BuyerResponseDto responseDto = buyerService.findById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<BuyerResponseDto> createBuyer(@RequestBody BuyerRequestDto buyer) {
        BuyerResponseDto responseDto = buyerService.save(buyer);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable UUID id) {
        buyerService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<BuyerResponseDto>> getAllBuyers(
            @ModelAttribute BuyerFilter filter,
            @PageableDefault(size = 10, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<BuyerResponseDto> responseDto = buyerService.findAllByFilter(filter, pageable);
        return ResponseEntity.ok(responseDto);
    }
}
