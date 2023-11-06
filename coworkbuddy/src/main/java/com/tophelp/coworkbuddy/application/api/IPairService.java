package com.tophelp.coworkbuddy.application.api;

import com.tophelp.coworkbuddy.infrastructure.dto.input.PairListInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.PairListDto;

public interface IPairService {


  void deleteAllPairs();

  PairListDto recommendPairs();

  PairListDto createOrUpdatePairs(PairListInputDto pairListInputDto);
}
