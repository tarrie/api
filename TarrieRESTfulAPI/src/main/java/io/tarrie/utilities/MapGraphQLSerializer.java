package io.tarrie.utilities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.json.JSONArray;

import java.io.IOException;
import java.util.Collection;
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

    System.out.println("SERIALIZE");
    System.out.println(map.toString());

    StringBuilder str = new StringBuilder();
    str.append("{");

    Iterator<Map> it = map.keySet().iterator();

    while (it.hasNext()) {
      Object key = it.next();
      Object value = map.get(key);

      if (value instanceof String) {
        str.append(String.format("\"%s\":\"%s\"", (String) key, (String) value));
        if (it.hasNext()) {
          str.append(",");
        }
      }

      if (value instanceof Integer) {
        str.append(String.format("\"%s\":%d", (String) key, (Integer) value));
        if (it.hasNext()) {
          str.append(",");
        }
      }

      if (value instanceof Float) {
        str.append(String.format("\"%s\":%f", (String) key, (Float) value));
        if (it.hasNext()) {
          str.append(",");
        }
      }

      if (value instanceof Collection<?>) {

        str.append(String.format("\"%s\":%s", (String) key, new JSONArray((Collection<?>) value).toString()));
        if (it.hasNext()) {
          str.append(",");
        }
      }


      if (value instanceof Map<?, ?>) {
                str.append(String.format("\"%s\":%s", (String) key,Utility.mapToString((Map<?, ?>) value) ));
        if (it.hasNext()) {
          str.append(",");
        }
      }
    }

    str.append("}");
    jsonGenerator.writeString(str.toString());
  }
}
