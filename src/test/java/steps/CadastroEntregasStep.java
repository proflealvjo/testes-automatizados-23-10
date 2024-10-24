package steps;

import com.networknt.schema.ValidationMessage;
import io.cucumber.java.bs.A;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import model.ErrorMessageModel;
import org.junit.Assert;
import services.CadastroEntregasService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CadastroEntregasStep {

    CadastroEntregasService cadastroEntregasService = new CadastroEntregasService();

    @Dado("que eu tenha os seguintes dados de entrega:")
    public void queEuTenhaOsSeguintesDadosDeEntrega(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            cadastroEntregasService.setFieldsDelivery(columns.get("campo"), columns.get("valor"));
        }
    }

    @Quando("eu enviar a requisicao para o endpoint {string} para cadastro de entrega")
    public void euEnviarARequisicaoParaOEndpointParaCadastroDeEntrega(String endPoint) {
        cadastroEntregasService.createDelivery(endPoint);
    }

    @Então("o status code deve retornar {int}")
    public void oStatusCodeDeveRetornar(int statusCode) {
        Assert.assertEquals(statusCode, cadastroEntregasService.response.statusCode());
    }

    @E("o corpo de resposta de erro da api deve retornar a mensagem {string}")
    public void oCorpoDeRespostaDeErroDaApiDeveRetornarAMensagem(String message) {
        ErrorMessageModel errorMessageModel = cadastroEntregasService.gson.fromJson(
                cadastroEntregasService.response.jsonPath().prettify(), ErrorMessageModel.class);
        Assert.assertEquals(message, errorMessageModel.getMessage());
    }

    @Dado("que eu recupere o ID da entrega criado no contexto")
    public void queEuRecupereOIDDaEntregaCriadoNoContexto() {
        cadastroEntregasService.retrieveIdDelivery();
    }

    @Quando("eu enviar a requisicao com o ID para o endpoint {string} de delecao de entrega")
    public void euEnviarARequisicaoComOIDParaOEndpointDeDelecaoDeEntrega(String endPoint) {
        cadastroEntregasService.deleteDelivery(endPoint);
    }

    @E("que o arquivo de contrato esperado é o {string}")
    public void queOArquivoDeContratoEsperadoÉO(String contract) throws IOException {
        cadastroEntregasService.setContract(contract);
    }

    @Então("a resposta da requisição deve estar em conformidade com o contrato selecionado")
    public void aRespostaDaRequisiçãoDeveEstarEmConformidadeComOContratoSelecionado() throws IOException {
        Set<ValidationMessage> validateResponse = cadastroEntregasService.validateResponseAgainstSchema();
        Assert.assertTrue("O contrato está inválido. Erros encontrados: " + validateResponse, validateResponse.isEmpty());
    }
}
