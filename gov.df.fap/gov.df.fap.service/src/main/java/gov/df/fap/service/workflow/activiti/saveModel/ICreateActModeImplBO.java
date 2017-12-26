package gov.df.fap.service.workflow.activiti.saveModel;

import gov.df.fap.api.workflow.activiti.ModelDataJsonConstants;
import gov.df.fap.api.workflow.activiti.saveModel.ICreateActMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class ICreateActModeImplBO implements ICreateActMode,ModelDataJsonConstants {
  
  @Override
  public void saveActModel(String modelId, MultiValueMap<String, String> values, RepositoryService repositoryService,
    ObjectMapper objectMapper,String proCode,boolean isFirstCreate) {
    
    ObjectMapper resultModel = new ObjectMapper();
    ObjectNode resultNode = resultModel.createObjectNode();
    
    Model model = repositoryService.getModel(modelId);
    try {
        if(isFirstCreate){
            JsonNode editorNode = new ObjectMapper().readTree(values
              .getFirst("json_xml").getBytes("utf-8"));
             JsonNode jsonNode = editorNode.get("properties");
             JsonNode proCodeValue = jsonNode.get("process_id");
             JsonNode processnameJson = jsonNode.get("processname");
            
            //创建model对象
            model = repositoryService.newModel();
            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processnameJson.asText());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "");
            model.setMetaInfo(modelObjectNode.toString());
            model.setName(processnameJson.asText());
//            model.setKey(proCodeValue.asText());
         }
       
        //正常更新
        ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model
        .getMetaInfo());

         modelJson.put(MODEL_NAME, values.getFirst("name"));
         modelJson
             .put(MODEL_DESCRIPTION, values.getFirst("description"));
         model.setMetaInfo(modelJson.toString());
         model.setName(values.getFirst("name"));
         model.setKey(proCode);
         repositoryService.saveModel(model);
       
         if(model.getLastUpdateTime()!=null){
           resultNode.put("update_time", model.getLastUpdateTime()
             .toString());
         }
       
         repositoryService.addModelEditorSource(model.getId(), values
             .getFirst("json_xml").getBytes("utf-8"));
       
         InputStream svgStream = new ByteArrayInputStream(values
             .getFirst("svg_xml").getBytes("utf-8"));
         TranscoderInput input = new TranscoderInput(svgStream);
         // 产生一个PNG图像
         PNGTranscoder transcoder = new PNGTranscoder();
         // Setup output
         // 可以捕获内存缓冲区的数据，转换成字节数组
         ByteArrayOutputStream outStream = new ByteArrayOutputStream();
         // 图像代码转换器API
         TranscoderOutput output = new TranscoderOutput(outStream);
         // Do the 转化
         transcoder.transcode(input, output);
       
         final byte[] result = outStream.toByteArray();
         repositoryService.addModelEditorSourceExtra(model.getId(),
             result);
         outStream.close();
        
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
