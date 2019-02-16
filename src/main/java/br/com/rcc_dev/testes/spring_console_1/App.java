package br.com.rcc_dev.testes.spring_console_1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.rcc_dev.testes.spring_console_1.entities.Config;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableConfigurationProperties
@Slf4j
public class App implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	// ----------------------------------------

	@Autowired
	private Config config;

	@Override
	public void run(String... args) throws Exception {
		log.info("Iniciando App. Config: {}", config);

		// ----
		Options options = new Options();
		options.addOption("h", "help", false, "Apresenta esta mensagem de ajuda.");
		CommandLine commands = new DefaultParser().parse(options, args);
		// ----

		if (commands.hasOption('h')) {
			HelpFormatter formatter = new HelpFormatter();
			final String lf = formatter.getNewLine();
			log.info("HELP:" + lf);
			formatter.printHelp(120, "this.jar", lf + "Podem ser usados os seguintes comandos:" + lf, options,
					lf + "Para mais informações consulte a documentação." + lf, true);

		}

		log.info("Terminando App");
	}

	// ----------------------------------------

	public static String contents(String arquivo) {
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(App.class.getClassLoader().getResource(arquivo).toURI()));
			return new String(bytes, Charset.forName("UTF-8"));
		} catch (IOException | URISyntaxException e) {
			log.error("Erro ao ler arquivo '{}' de dentr do JAR!", arquivo, e);
			throw new RuntimeException(String.format("Erro ao ler arquivo '%s' de dentro do JAR!", arquivo), e);
		}
	}

}

