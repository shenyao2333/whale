// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: MsgStatus.proto

package com.whale.generator.netty.common.protocol;

public final class Status {
  private Status() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  /**
   * <pre>
   **
   * 指令类型
   * </pre>
   *
   * Protobuf enum {@code MsgStatus}
   */
  public enum MsgStatus
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <pre>
     *未读
     * </pre>
     *
     * <code>ALREADY = 0;</code>
     */
    ALREADY(0),
    /**
     * <pre>
     *已读
     * </pre>
     *
     * <code>READ = 1;</code>
     */
    READ(1),
    /**
     * <pre>
     *撤回
     * </pre>
     *
     * <code>RECALL = 2;</code>
     */
    RECALL(2),
    /**
     * <pre>
     *删除
     * </pre>
     *
     * <code>REMOVE = 3;</code>
     */
    REMOVE(3),
    ;

    /**
     * <pre>
     *未读
     * </pre>
     *
     * <code>ALREADY = 0;</code>
     */
    public static final int ALREADY_VALUE = 0;
    /**
     * <pre>
     *已读
     * </pre>
     *
     * <code>READ = 1;</code>
     */
    public static final int READ_VALUE = 1;
    /**
     * <pre>
     *撤回
     * </pre>
     *
     * <code>RECALL = 2;</code>
     */
    public static final int RECALL_VALUE = 2;
    /**
     * <pre>
     *删除
     * </pre>
     *
     * <code>REMOVE = 3;</code>
     */
    public static final int REMOVE_VALUE = 3;


    public final int getNumber() {
      return value;
    }

    /**
     * @param value The numeric wire value of the corresponding enum entry.
     * @return The enum associated with the given numeric wire value.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static MsgStatus valueOf(int value) {
      return forNumber(value);
    }

    /**
     * @param value The numeric wire value of the corresponding enum entry.
     * @return The enum associated with the given numeric wire value.
     */
    public static MsgStatus forNumber(int value) {
      switch (value) {
        case 0: return ALREADY;
        case 1: return READ;
        case 2: return RECALL;
        case 3: return REMOVE;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<MsgStatus>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        MsgStatus> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<MsgStatus>() {
            public MsgStatus findValueByNumber(int number) {
              return MsgStatus.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return com.whale.generator.netty.common.protocol.Status.getDescriptor().getEnumTypes().get(0);
    }

    private static final MsgStatus[] VALUES = values();

    public static MsgStatus valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private MsgStatus(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:MsgStatus)
  }


  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017MsgStatus.proto*:\n\tMsgStatus\022\013\n\007ALREAD" +
      "Y\020\000\022\010\n\004READ\020\001\022\n\n\006RECALL\020\002\022\n\n\006REMOVE\020\003B3\n" +
      ")com.whale.generator.netty.common.protoc" +
      "olB\006Status"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
