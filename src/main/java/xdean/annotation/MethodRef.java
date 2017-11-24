package xdean.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotated on annotation's attribute to indicate that it is referenced to a method.
 *
 * @see Type
 * @author XDean
 */
@Documented
@Retention(SOURCE)
@Target(METHOD)
public @interface MethodRef {

  public enum Type {
    /**
     * Annotated on a String attribute, its value will be parsed to class name and method name.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
    //define
    &#64;interface UseAll {
      &#64;MethodRef //default type is All
      String value();
    }

    //usage
    &#64;UseAll("java.lang.String::length")
    void func();

    </code>
     * </pre>
     *
     * @see MethodRef#splitor()
     */
    ALL,
    /**
     * Only used with {@link Type#METHOD}
     *
     * @see Type#METHOD
     */
    CLASS,
    /**
     * Annotated on a String attribute, it has following modes:
     * <ul>
     * <li>Use with another attribute with {@link Type#CLASS}, reference method from class by the other attribute's
     * value</li>
     * <li>Use with {@link MethodRef#defaultClass()}, reference method from the determined class</li>
     * <li>Use with {@link MethodRef#parentClass()}, reference method from class by its EnclosingElement(usually a
     * class)'s annotation's value</li>
     * </ul>
     * <p>
     * Example: <br>
     * 1. Note that if you use Class and Method, there must have and only have 2 attribute with {@link &#64;MethodRef}
     *
     * <pre>
     * <code>
    //define
    &#64;interface UseClassAndMethod {
      &#64;MethodRef(type = Type.CLASS)
      Class<? extends Number> type();

      &#64;MethodRef(type = Type.METHOD)
      String method();
    }

    //usage
    &#64;UseClassAndMethod(type = Integer.class, method = "intValue")
    void func();
    </code>
     * </pre>
     *
     * 2.
     *
     * <pre>
     * <code>
    //define
    &#64;interface UseDefaultClass {
      &#64;MethodRef(type = Type.METHOD, defaultClass = String.class)
      String method();
    }

    //usage
    &#64;UseDefaultClass(method = "length")
    void func();
    </code>
     * </pre>
     *
     * 3.
     *
     * <pre>
     * <code>
    //define
    &#64;interface UseParentClass {
      &#64;interface Parent {
        Class<?> value();
      }

      &#64;MethodRef(type = Type.METHOD, parentClass = Parent.class)
      String method();
    }

    //usage
    &#64;Parent(String.class)
    class Bar{
      &#64;UseParentClass(method = "length")
      void func();
    }
    </code>
     * </pre>
     */
    METHOD
  }

  /**
   * @see Type
   */
  Type type() default Type.ALL;

  /**
   * If using {@link Type#ALL}, the attribute value will be split by splitor.<br>
   * For example: "java.lang.String:length" with splitor ':'.
   *
   * @see Type#METHOD
   */
  char splitor() default ':';

  /**
   * @see Type#METHOD
   */
  Class<? extends Annotation> parentClass() default MethodRef.class;

  /**
   * @see Type#METHOD
   */
  Class<?> defaultClass() default Void.class;
}
