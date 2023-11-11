package com.tophelp.coworkbuddy.application.api;

import com.tophelp.coworkbuddy.infrastructure.dto.input.PairListInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.PairListDto;

public interface IPairService {

  void deleteAllPairs();

  PairListDto recommendPairs(String roomId);

  PairListDto createOrUpdatePairs(PairListInputDto pairListInputDto);

  PairListDto retrieveCurrentPairsByRoomId(String roomId);
}
