package io.tarrie.utilities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class MapGraphQLSerializer extends StdSerializer<Map> {

  protected MapGraphQLSerializer(Class<Map> t) {
    super(t);
  }

  public MapGraphQLSerializer() {
    this(null);
  }

  @Override
  public void serialize(Map map, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {


    StringBuilder str = new StringBuilder();
    str.append("{");

    Iterator<Map> it = map.keySet().iterator();

    while (it.hasNext()) {
      Object key = it.next();

      if (map.get(key) instanceof String) {
        str.append(String.format("\"%s\":\"%s\"", (String) key, (String) map.get(key)));
        if (it.hasNext()) {
          str.append(",");
        }
      }

      if (map.get(key) instanceof Integer) {
        str.append(String.format("\"%s\":%d", (String) key, (Integer) map.get(key)));
        if (it.hasNext()) {
          str.append(",");
        }
      }

      if (map.get(key) instanceof Float) {
        str.append(String.format("\"%s\":%f", (String) key, (Float) map.get(key)));
        if (it.hasNext()) {
          str.append(",");
        }
      }
    }

    str.append("}");
    jsonGenerator.writeString(str.toString());
  }
}
