package com.example.kafkaopensearchproject.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class idStreamData {


    private String topic;
    private int partition;
    private long timestamp;
    private int offset;
}
