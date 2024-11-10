package com.microservice.viajes.http.response;

import com.microservice.viajes.dto.MonopatinDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class findMonopatinById {

    private MonopatinDTO monopatin;
}
