package com.tophelp.coworkbuddy.ui.controller;

import com.tophelp.coworkbuddy.application.api.IPairService;
import com.tophelp.coworkbuddy.infrastructure.dto.input.PairListInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.PairListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/pairs")
@RequiredArgsConstructor
public class PairResource {

  private final IPairService pairService;

  @PostMapping
  public ResponseEntity<PairListDto> createOrUpdatePairs(@RequestBody PairListInputDto pairListInputDto) {
    log.info("PairResource - createOrUpdatePairs");
    return ResponseEntity.ok(pairService.createOrUpdatePairs(pairListInputDto));
  }

  @GetMapping("/recommend")
  public ResponseEntity<PairListDto> recommendPairs() {
    log.info("PairResource - deleteAllPairs");
    return ResponseEntity.ok(pairService.recommendPairs());
  }

  @DeleteMapping("/reset/all")
  public ResponseEntity<Void> deleteAllPairs() {
    log.info("PairResource - deleteAllPairs");
    pairService.deleteAllPairs();
    return ResponseEntity.noContent().build();
  }

}
