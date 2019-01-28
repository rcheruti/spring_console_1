package br.com.rcc_dev.testes.spring_console_1.entities;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "config")
public class Config {
  
  private String name = "";

}