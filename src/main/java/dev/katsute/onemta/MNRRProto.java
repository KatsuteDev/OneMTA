// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: gtfs-realtime-MNR.proto

package dev.katsute.onemta;

@SuppressWarnings("all")
abstract class MNRRProto {
  private MNRRProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
    registry.add(dev.katsute.onemta.MNRRProto.mnrStopTimeUpdate);
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MnrStopTimeUpdateOrBuilder extends
      // @@protoc_insertion_point(interface_extends:transit_realtime.MnrStopTimeUpdate)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     *can add additional fields here without having to //extend StopTimeUpdate again
     * </pre>
     *
     * <code>optional string track = 1;</code>
     * @return Whether the track field is set.
     */
    boolean hasTrack();
    /**
     * <pre>
     *can add additional fields here without having to //extend StopTimeUpdate again
     * </pre>
     *
     * <code>optional string track = 1;</code>
     * @return The track.
     */
    java.lang.String getTrack();
    /**
     * <pre>
     *can add additional fields here without having to //extend StopTimeUpdate again
     * </pre>
     *
     * <code>optional string track = 1;</code>
     * @return The bytes for track.
     */
    com.google.protobuf.ByteString
        getTrackBytes();

    /**
     * <code>optional string trainStatus = 2;</code>
     * @return Whether the trainStatus field is set.
     */
    boolean hasTrainStatus();
    /**
     * <code>optional string trainStatus = 2;</code>
     * @return The trainStatus.
     */
    java.lang.String getTrainStatus();
    /**
     * <code>optional string trainStatus = 2;</code>
     * @return The bytes for trainStatus.
     */
    com.google.protobuf.ByteString
        getTrainStatusBytes();
  }
  /**
   * Protobuf type {@code transit_realtime.MnrStopTimeUpdate}
   */
  public static final class MnrStopTimeUpdate extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:transit_realtime.MnrStopTimeUpdate)
      MnrStopTimeUpdateOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use MnrStopTimeUpdate.newBuilder() to construct.
    private MnrStopTimeUpdate(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private MnrStopTimeUpdate() {
      track_ = "";
      trainStatus_ = "";
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
        UnusedPrivateParameter unused) {
      return new MnrStopTimeUpdate();
    }

    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return dev.katsute.onemta.MNRRProto.internal_static_transit_realtime_MnrStopTimeUpdate_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return dev.katsute.onemta.MNRRProto.internal_static_transit_realtime_MnrStopTimeUpdate_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate.class, dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate.Builder.class);
    }

    private int bitField0_;
    public static final int TRACK_FIELD_NUMBER = 1;
    @SuppressWarnings("serial")
    private volatile java.lang.Object track_ = "";
    /**
     * <pre>
     *can add additional fields here without having to //extend StopTimeUpdate again
     * </pre>
     *
     * <code>optional string track = 1;</code>
     * @return Whether the track field is set.
     */
    @java.lang.Override
    public boolean hasTrack() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <pre>
     *can add additional fields here without having to //extend StopTimeUpdate again
     * </pre>
     *
     * <code>optional string track = 1;</code>
     * @return The track.
     */
    @java.lang.Override
    public java.lang.String getTrack() {
      java.lang.Object ref = track_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          track_ = s;
        }
        return s;
      }
    }
    /**
     * <pre>
     *can add additional fields here without having to //extend StopTimeUpdate again
     * </pre>
     *
     * <code>optional string track = 1;</code>
     * @return The bytes for track.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getTrackBytes() {
      java.lang.Object ref = track_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        track_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int TRAINSTATUS_FIELD_NUMBER = 2;
    @SuppressWarnings("serial")
    private volatile java.lang.Object trainStatus_ = "";
    /**
     * <code>optional string trainStatus = 2;</code>
     * @return Whether the trainStatus field is set.
     */
    @java.lang.Override
    public boolean hasTrainStatus() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <code>optional string trainStatus = 2;</code>
     * @return The trainStatus.
     */
    @java.lang.Override
    public java.lang.String getTrainStatus() {
      java.lang.Object ref = trainStatus_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          trainStatus_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string trainStatus = 2;</code>
     * @return The bytes for trainStatus.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getTrainStatusBytes() {
      java.lang.Object ref = trainStatus_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        trainStatus_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate parseFrom(
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
    public static Builder newBuilder(dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate prototype) {
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
     * Protobuf type {@code transit_realtime.MnrStopTimeUpdate}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:transit_realtime.MnrStopTimeUpdate)
        dev.katsute.onemta.MNRRProto.MnrStopTimeUpdateOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return dev.katsute.onemta.MNRRProto.internal_static_transit_realtime_MnrStopTimeUpdate_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return dev.katsute.onemta.MNRRProto.internal_static_transit_realtime_MnrStopTimeUpdate_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate.class, dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate.Builder.class);
      }

      // Construct using dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate.newBuilder()
      private Builder() {

      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);

      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        track_ = "";
        trainStatus_ = "";
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return dev.katsute.onemta.MNRRProto.internal_static_transit_realtime_MnrStopTimeUpdate_descriptor;
      }

      @java.lang.Override
      public dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate getDefaultInstanceForType() {
        return dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate.getDefaultInstance();
      }

      @java.lang.Override
      public dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate build() {
        dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate buildPartial() {
        dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate result = new dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate(this);
        if (bitField0_ != 0) { buildPartial0(result); }
        onBuilt();
        return result;
      }

      private void buildPartial0(dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate result) {
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.track_ = track_;
          to_bitField0_ |= 0x00000001;
        }
        if (((from_bitField0_ & 0x00000002) != 0)) {
          result.trainStatus_ = trainStatus_;
          to_bitField0_ |= 0x00000002;
        }
        result.bitField0_ |= to_bitField0_;
      }

      private int bitField0_;

      private java.lang.Object track_ = "";
      /**
       * <pre>
       *can add additional fields here without having to //extend StopTimeUpdate again
       * </pre>
       *
       * <code>optional string track = 1;</code>
       * @return Whether the track field is set.
       */
      public boolean hasTrack() {
        return ((bitField0_ & 0x00000001) != 0);
      }
      /**
       * <pre>
       *can add additional fields here without having to //extend StopTimeUpdate again
       * </pre>
       *
       * <code>optional string track = 1;</code>
       * @return The track.
       */
      public java.lang.String getTrack() {
        java.lang.Object ref = track_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            track_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       *can add additional fields here without having to //extend StopTimeUpdate again
       * </pre>
       *
       * <code>optional string track = 1;</code>
       * @return The bytes for track.
       */
      public com.google.protobuf.ByteString
          getTrackBytes() {
        java.lang.Object ref = track_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          track_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *can add additional fields here without having to //extend StopTimeUpdate again
       * </pre>
       *
       * <code>optional string track = 1;</code>
       * @param value The track to set.
       * @return This builder for chaining.
       */
      public Builder setTrack(
          java.lang.String value) {
        if (value == null) { throw new NullPointerException(); }
        track_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *can add additional fields here without having to //extend StopTimeUpdate again
       * </pre>
       *
       * <code>optional string track = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearTrack() {
        track_ = getDefaultInstance().getTrack();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      /**
       * <pre>
       *can add additional fields here without having to //extend StopTimeUpdate again
       * </pre>
       *
       * <code>optional string track = 1;</code>
       * @param value The bytes for track to set.
       * @return This builder for chaining.
       */
      public Builder setTrackBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        track_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }

      private java.lang.Object trainStatus_ = "";
      /**
       * <code>optional string trainStatus = 2;</code>
       * @return Whether the trainStatus field is set.
       */
      public boolean hasTrainStatus() {
        return ((bitField0_ & 0x00000002) != 0);
      }
      /**
       * <code>optional string trainStatus = 2;</code>
       * @return The trainStatus.
       */
      public java.lang.String getTrainStatus() {
        java.lang.Object ref = trainStatus_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            trainStatus_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string trainStatus = 2;</code>
       * @return The bytes for trainStatus.
       */
      public com.google.protobuf.ByteString
          getTrainStatusBytes() {
        java.lang.Object ref = trainStatus_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          trainStatus_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string trainStatus = 2;</code>
       * @param value The trainStatus to set.
       * @return This builder for chaining.
       */
      public Builder setTrainStatus(
          java.lang.String value) {
        if (value == null) { throw new NullPointerException(); }
        trainStatus_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }
      /**
       * <code>optional string trainStatus = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearTrainStatus() {
        trainStatus_ = getDefaultInstance().getTrainStatus();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      /**
       * <code>optional string trainStatus = 2;</code>
       * @param value The bytes for trainStatus to set.
       * @return This builder for chaining.
       */
      public Builder setTrainStatusBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        trainStatus_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
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

      // @@protoc_insertion_point(builder_scope:transit_realtime.MnrStopTimeUpdate)
    }

    // @@protoc_insertion_point(class_scope:transit_realtime.MnrStopTimeUpdate)
    private static final dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate();
    }

    public static dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<MnrStopTimeUpdate>
        PARSER = new com.google.protobuf.AbstractParser<MnrStopTimeUpdate>() {
      @java.lang.Override
      public MnrStopTimeUpdate parsePartialFrom(
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

    public static com.google.protobuf.Parser<MnrStopTimeUpdate> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<MnrStopTimeUpdate> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  public static final int MNR_STOP_TIME_UPDATE_FIELD_NUMBER = 1005;
  /**
   * <code>extend .transit_realtime.TripUpdate.StopTimeUpdate { ... }</code>
   */
  public static final
    com.google.protobuf.GeneratedMessage.GeneratedExtension<
      dev.katsute.onemta.GTFSRealtimeProto.TripUpdate.StopTimeUpdate,
      dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate> mnrStopTimeUpdate = com.google.protobuf.GeneratedMessage
          .newFileScopedGeneratedExtension(
        dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate.class,
        dev.katsute.onemta.MNRRProto.MnrStopTimeUpdate.getDefaultInstance());
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_transit_realtime_MnrStopTimeUpdate_descriptor;
  private static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_transit_realtime_MnrStopTimeUpdate_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\027gtfs-realtime-MNR.proto\022\020transit_realt" +
      "ime\032\023gtfs-realtime.proto\"7\n\021MnrStopTimeU" +
      "pdate\022\r\n\005track\030\001 \001(\t\022\023\n\013trainStatus\030\002 \001(" +
      "\t:o\n\024mnr_stop_time_update\022+.transit_real" +
      "time.TripUpdate.StopTimeUpdate\030\355\007 \001(\0132#." +
      "transit_realtime.MnrStopTimeUpdateB!\n\022de" +
      "v.katsute.onemtaB\tMNRRProtoH\002"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          dev.katsute.onemta.GTFSRealtimeProto.getDescriptor(),
        });
    internal_static_transit_realtime_MnrStopTimeUpdate_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_transit_realtime_MnrStopTimeUpdate_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_transit_realtime_MnrStopTimeUpdate_descriptor,
        new java.lang.String[] { "Track", "TrainStatus", });
    mnrStopTimeUpdate.internalInit(descriptor.getExtensions().get(0));
    dev.katsute.onemta.GTFSRealtimeProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}