package br.com.rcc_dev.testes.spring_console_1;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

//@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AppTests {

	@Value("${correios.url}")
	private String correiosUrl;

	// =============================================

	@Test
	@Ignore
	public void requisicaoUnirest() throws UnirestException {
		String correiosXml = App.contents("consultaCep.xml").replace("$cep", "02060-001");
		// Docs: http://unirest.io/java.html
		String resp = Unirest.post(correiosUrl).body(correiosXml).asString().getBody();
		log.info("Resposta: \n{}", resp);
		// Unirest.shutdown();
	}

	@Test
	//@Ignore
	public void requisicaoUnirestAsync() throws UnirestException, InterruptedException, ExecutionException, IOException {
		String correiosXml = App.contents("consultaCep.xml").replace("$cep", "02060-001");
		// Docs: http://unirest.io/java.html
		Unirest.setConcurrency(4, 4);

		Unirest.post(correiosUrl).body(correiosXml).asStringAsync(new Call<String>(response -> {
			log.info("Unirest...( completed 1 )");
			String resp = response.getBody();
			log.info("Resposta: \n{}", resp );
		}));
		Unirest.post(correiosUrl).body(correiosXml).asStringAsync(new Call<String>(response -> {
			log.info("Unirest...( completed 2 )");
			String resp = response.getBody();
			log.info("Resposta: \n{}", resp );
		}));
		Unirest.post(correiosUrl).body(correiosXml).asStringAsync(new Call<String>(response -> {
			log.info("Unirest...( completed 3 )");
			String resp = response.getBody();
			log.info("Resposta: \n{}", resp );
		}));
		log.info("Unirest...asStringAsync executado");
		
		Thread.currentThread().sleep(3000);
		Unirest.shutdown(); 
	}

	@Test
	@Ignore
	public void requisicaoOkHttp() throws IOException {
		String correiosXml = App.contents("consultaCep.xml").replace("$cep", "02060-001");
		// ---
		// Docs: https://square.github.io/okhttp/
		// O "Client" guarda as configs de Threads e Pools (para sync e async)
		// O ideal é ter apenas um Client por aplicação! (para evitar criar vários Pools e configs de Threads)
		OkHttpClient client = new OkHttpClient.Builder()
			.addInterceptor(new HttpLoggingInterceptor().setLevel(Level.BODY))
			.build();

		try( Response resp =
					client.newCall(
					new Request.Builder()
						.url(correiosUrl)
						.post(RequestBody.create(MediaType.parse("application/json"), correiosXml)).build())
						.execute() ){
			String dados = resp.body().string();
		}
				
	}


	// -----------------------------

	@Slf4j
	public static class Call<T> implements Callback<T> {

		private Consumer<HttpResponse<T>> completed;
		private Consumer<UnirestException> failed;
		private Runnable cancelled;

		public Call(Consumer<HttpResponse<T>> completed){ this(completed, null, null); }
		public Call(Consumer<HttpResponse<T>> completed, Consumer<UnirestException> failed){ this(completed, failed, null); }
		public Call(Consumer<HttpResponse<T>> completed, Consumer<UnirestException> failed, Runnable cancelled){
			this.completed = completed;
			this.failed = failed;
			this.cancelled = cancelled;
		}

		@Override
		public void completed(HttpResponse<T> response) {
			if( completed != null ){
				completed.accept(response);
			}
		}
		@Override
		public void failed(UnirestException e) {
			if( failed != null ){
				failed.accept(e);
			} else {
				log.error("Erro ao executar requisição HTTP!", e);
			}
		}
		@Override
		public void cancelled() {
			if( cancelled != null ){
				cancelled.run();
			} else {
				log.warn("A requisição HTTP foi cancelada!");
			}
		}
	} 

}

