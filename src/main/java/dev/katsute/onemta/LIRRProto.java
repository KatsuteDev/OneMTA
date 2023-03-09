// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: gtfs-realtime-LIRR.proto

package dev.katsute.onemta;

@SuppressWarnings("all")
abstract class LIRRProto {
  private LIRRProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
    registry.add(dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate.track);
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MtaStopTimeUpdateOrBuilder extends
      // @@protoc_insertion_point(interface_extends:transit_realtime.MtaStopTimeUpdate)
      com.google.protobuf.MessageOrBuilder {
  }
  /**
   * Protobuf type {@code transit_realtime.MtaStopTimeUpdate}
   */
  public static final class MtaStopTimeUpdate extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:transit_realtime.MtaStopTimeUpdate)
      MtaStopTimeUpdateOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use MtaStopTimeUpdate.newBuilder() to construct.
    private MtaStopTimeUpdate(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private MtaStopTimeUpdate() {
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
        UnusedPrivateParameter unused) {
      return new MtaStopTimeUpdate();
    }

    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return dev.katsute.onemta.LIRRProto.internal_static_transit_realtime_MtaStopTimeUpdate_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return dev.katsute.onemta.LIRRProto.internal_static_transit_realtime_MtaStopTimeUpdate_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate.class, dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate.Builder.class);
    }

    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code transit_realtime.MtaStopTimeUpdate}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:transit_realtime.MtaStopTimeUpdate)
        dev.katsute.onemta.LIRRProto.MtaStopTimeUpdateOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return dev.katsute.onemta.LIRRProto.internal_static_transit_realtime_MtaStopTimeUpdate_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return dev.katsute.onemta.LIRRProto.internal_static_transit_realtime_MtaStopTimeUpdate_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate.class, dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate.Builder.class);
      }

      // Construct using dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate.newBuilder()
      private Builder() {

      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);

      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return dev.katsute.onemta.LIRRProto.internal_static_transit_realtime_MtaStopTimeUpdate_descriptor;
      }

      @java.lang.Override
      public dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate getDefaultInstanceForType() {
        return dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate.getDefaultInstance();
      }

      @java.lang.Override
      public dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate build() {
        dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate buildPartial() {
        dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate result = new dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate(this);
        onBuilt();
        return result;
      }

      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:transit_realtime.MtaStopTimeUpdate)
    }

    // @@protoc_insertion_point(class_scope:transit_realtime.MtaStopTimeUpdate)
    private static final dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate();
    }

    public static dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<MtaStopTimeUpdate>
        PARSER = new com.google.protobuf.AbstractParser<MtaStopTimeUpdate>() {
      @java.lang.Override
      public MtaStopTimeUpdate parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        Builder builder = newBuilder();
        try {
          builder.mergeFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(builder.buildPartial());
        } catch (com.google.protobuf.UninitializedMessageException e) {
          throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(e)
              .setUnfinishedMessage(builder.buildPartial());
        }
        return builder.buildPartial();
      }
    };

    public static com.google.protobuf.Parser<MtaStopTimeUpdate> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<MtaStopTimeUpdate> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

    public static final int TRACK_FIELD_NUMBER = 1005;
    /**
     * <pre>
     *can add additional fields here without having to extend StopTimeUpdate again
     * </pre>
     *
     * <code>extend .transit_realtime.TripUpdate.StopTimeUpdate { ... }</code>
     */
    public static final
      com.google.protobuf.GeneratedMessage.GeneratedExtension<
        dev.katsute.onemta.GTFSRealtimeProto.TripUpdate.StopTimeUpdate,
        java.lang.String> track = com.google.protobuf.GeneratedMessage
            .newMessageScopedGeneratedExtension(
          dev.katsute.onemta.LIRRProto.MtaStopTimeUpdate.getDefaultInstance(),
          0,
          java.lang.String.class,
          null);
  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_transit_realtime_MtaStopTimeUpdate_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_transit_realtime_MtaStopTimeUpdate_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\030gtfs-realtime-LIRR.proto\022\020transit_real" +
      "time\032\023gtfs-realtime.proto\"P\n\021MtaStopTime" +
      "Update2;\n\005track\022+.transit_realtime.TripU" +
      "pdate.StopTimeUpdate\030\355\007 \001(\tB!\n\022dev.katsu" +
      "te.onemtaB\tLIRRProtoH\002"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          dev.katsute.onemta.GTFSRealtimeProto.getDescriptor(),
        });
    internal_static_transit_realtime_MtaStopTimeUpdate_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_transit_realtime_MtaStopTimeUpdate_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_transit_realtime_MtaStopTimeUpdate_descriptor,
        new java.lang.String[] { });
    dev.katsute.onemta.GTFSRealtimeProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
