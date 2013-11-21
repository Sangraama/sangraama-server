/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.sangraama.thrift.assets;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.EncodingUtils;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;

public class TPlayer implements org.apache.thrift.TBase<TPlayer, TPlayer._Fields>,
        java.io.Serializable, Cloneable {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(
            "TPlayer");

    private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField(
            "id", org.apache.thrift.protocol.TType.I64, (short) 1);
    private static final org.apache.thrift.protocol.TField X_FIELD_DESC = new org.apache.thrift.protocol.TField(
            "x", org.apache.thrift.protocol.TType.I32, (short) 2);
    private static final org.apache.thrift.protocol.TField Y_FIELD_DESC = new org.apache.thrift.protocol.TField(
            "y", org.apache.thrift.protocol.TType.I32, (short) 3);
    private static final org.apache.thrift.protocol.TField V_X_FIELD_DESC = new org.apache.thrift.protocol.TField(
            "v_x", org.apache.thrift.protocol.TType.DOUBLE, (short) 4);
    private static final org.apache.thrift.protocol.TField V_Y_FIELD_DESC = new org.apache.thrift.protocol.TField(
            "v_y", org.apache.thrift.protocol.TType.DOUBLE, (short) 5);

    private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
    static {
        schemes.put(StandardScheme.class, new TPlayerStandardSchemeFactory());
        schemes.put(TupleScheme.class, new TPlayerTupleSchemeFactory());
    }

    public long id; // required
    public int x; // required
    public int y; // required
    public double v_x; // required
    public double v_y; // required

    /**
     * The set of fields this struct contains, along with convenience methods for finding and
     * manipulating them.
     */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
        ID((short) 1, "id"), X((short) 2, "x"), Y((short) 3, "y"), V_X((short) 4, "v_x"), V_Y(
                (short) 5, "v_y");

        private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

        static {
            for (_Fields field : EnumSet.allOf(_Fields.class)) {
                byName.put(field.getFieldName(), field);
            }
        }

        /**
         * Find the _Fields constant that matches fieldId, or null if its not found.
         */
        public static _Fields findByThriftId(int fieldId) {
            switch (fieldId) {
                case 1: // ID
                    return ID;
                case 2: // X
                    return X;
                case 3: // Y
                    return Y;
                case 4: // V_X
                    return V_X;
                case 5: // V_Y
                    return V_Y;
                default:
                    return null;
            }
        }

        /**
         * Find the _Fields constant that matches fieldId, throwing an exception if it is not found.
         */
        public static _Fields findByThriftIdOrThrow(int fieldId) {
            _Fields fields = findByThriftId(fieldId);
            if (fields == null)
                throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
            return fields;
        }

        /**
         * Find the _Fields constant that matches name, or null if its not found.
         */
        public static _Fields findByName(String name) {
            return byName.get(name);
        }

        private final short _thriftId;
        private final String _fieldName;

        _Fields(short thriftId, String fieldName) {
            _thriftId = thriftId;
            _fieldName = fieldName;
        }

        public short getThriftFieldId() {
            return _thriftId;
        }

        public String getFieldName() {
            return _fieldName;
        }
    }

    // isset id assignments
    private static final int __ID_ISSET_ID = 0;
    private static final int __X_ISSET_ID = 1;
    private static final int __Y_ISSET_ID = 2;
    private static final int __V_X_ISSET_ID = 3;
    private static final int __V_Y_ISSET_ID = 4;
    private byte __isset_bitfield = 0;
    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    static {
        Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(
                _Fields.class);
        tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id",
                org.apache.thrift.TFieldRequirementType.DEFAULT,
                new org.apache.thrift.meta_data.FieldValueMetaData(
                        org.apache.thrift.protocol.TType.I64)));
        tmpMap.put(_Fields.X, new org.apache.thrift.meta_data.FieldMetaData("x",
                org.apache.thrift.TFieldRequirementType.DEFAULT,
                new org.apache.thrift.meta_data.FieldValueMetaData(
                        org.apache.thrift.protocol.TType.I32)));
        tmpMap.put(_Fields.Y, new org.apache.thrift.meta_data.FieldMetaData("y",
                org.apache.thrift.TFieldRequirementType.DEFAULT,
                new org.apache.thrift.meta_data.FieldValueMetaData(
                        org.apache.thrift.protocol.TType.I32)));
        tmpMap.put(_Fields.V_X, new org.apache.thrift.meta_data.FieldMetaData("v_x",
                org.apache.thrift.TFieldRequirementType.DEFAULT,
                new org.apache.thrift.meta_data.FieldValueMetaData(
                        org.apache.thrift.protocol.TType.DOUBLE)));
        tmpMap.put(_Fields.V_Y, new org.apache.thrift.meta_data.FieldMetaData("v_y",
                org.apache.thrift.TFieldRequirementType.DEFAULT,
                new org.apache.thrift.meta_data.FieldValueMetaData(
                        org.apache.thrift.protocol.TType.DOUBLE)));
        metaDataMap = Collections.unmodifiableMap(tmpMap);
        org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TPlayer.class, metaDataMap);
    }

    public TPlayer() {
    }

    public TPlayer(long id, int x, int y, double v_x, double v_y) {
        this();
        this.id = id;
        setIdIsSet(true);
        this.x = x;
        setXIsSet(true);
        this.y = y;
        setYIsSet(true);
        this.v_x = v_x;
        setV_xIsSet(true);
        this.v_y = v_y;
        setV_yIsSet(true);
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public TPlayer(TPlayer other) {
        __isset_bitfield = other.__isset_bitfield;
        this.id = other.id;
        this.x = other.x;
        this.y = other.y;
        this.v_x = other.v_x;
        this.v_y = other.v_y;
    }

    public TPlayer deepCopy() {
        return new TPlayer(this);
    }

    @Override
    public void clear() {
        setIdIsSet(false);
        this.id = 0;
        setXIsSet(false);
        this.x = 0;
        setYIsSet(false);
        this.y = 0;
        setV_xIsSet(false);
        this.v_x = 0.0;
        setV_yIsSet(false);
        this.v_y = 0.0;
    }

    public long getId() {
        return this.id;
    }

    public TPlayer setId(long id) {
        this.id = id;
        setIdIsSet(true);
        return this;
    }

    public void unsetId() {
        __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ID_ISSET_ID);
    }

    /** Returns true if field id is set (has been assigned a value) and false otherwise */
    public boolean isSetId() {
        return EncodingUtils.testBit(__isset_bitfield, __ID_ISSET_ID);
    }

    public void setIdIsSet(boolean value) {
        __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ID_ISSET_ID, value);
    }

    public int getX() {
        return this.x;
    }

    public TPlayer setX(int x) {
        this.x = x;
        setXIsSet(true);
        return this;
    }

    public void unsetX() {
        __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __X_ISSET_ID);
    }

    /** Returns true if field x is set (has been assigned a value) and false otherwise */
    public boolean isSetX() {
        return EncodingUtils.testBit(__isset_bitfield, __X_ISSET_ID);
    }

    public void setXIsSet(boolean value) {
        __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __X_ISSET_ID, value);
    }

    public int getY() {
        return this.y;
    }

    public TPlayer setY(int y) {
        this.y = y;
        setYIsSet(true);
        return this;
    }

    public void unsetY() {
        __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __Y_ISSET_ID);
    }

    /** Returns true if field y is set (has been assigned a value) and false otherwise */
    public boolean isSetY() {
        return EncodingUtils.testBit(__isset_bitfield, __Y_ISSET_ID);
    }

    public void setYIsSet(boolean value) {
        __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __Y_ISSET_ID, value);
    }

    public double getV_x() {
        return this.v_x;
    }

    public TPlayer setV_x(double v_x) {
        this.v_x = v_x;
        setV_xIsSet(true);
        return this;
    }

    public void unsetV_x() {
        __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __V_X_ISSET_ID);
    }

    /** Returns true if field v_x is set (has been assigned a value) and false otherwise */
    public boolean isSetV_x() {
        return EncodingUtils.testBit(__isset_bitfield, __V_X_ISSET_ID);
    }

    public void setV_xIsSet(boolean value) {
        __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __V_X_ISSET_ID, value);
    }

    public double getV_y() {
        return this.v_y;
    }

    public TPlayer setV_y(double v_y) {
        this.v_y = v_y;
        setV_yIsSet(true);
        return this;
    }

    public void unsetV_y() {
        __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __V_Y_ISSET_ID);
    }

    /** Returns true if field v_y is set (has been assigned a value) and false otherwise */
    public boolean isSetV_y() {
        return EncodingUtils.testBit(__isset_bitfield, __V_Y_ISSET_ID);
    }

    public void setV_yIsSet(boolean value) {
        __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __V_Y_ISSET_ID, value);
    }

    public void setFieldValue(_Fields field, Object value) {
        switch (field) {
            case ID:
                if (value == null) {
                    unsetId();
                } else {
                    setId((Long) value);
                }
                break;

            case X:
                if (value == null) {
                    unsetX();
                } else {
                    setX((Integer) value);
                }
                break;

            case Y:
                if (value == null) {
                    unsetY();
                } else {
                    setY((Integer) value);
                }
                break;

            case V_X:
                if (value == null) {
                    unsetV_x();
                } else {
                    setV_x((Double) value);
                }
                break;

            case V_Y:
                if (value == null) {
                    unsetV_y();
                } else {
                    setV_y((Double) value);
                }
                break;

        }
    }

    public Object getFieldValue(_Fields field) {
        switch (field) {
            case ID:
                return Long.valueOf(getId());

            case X:
                return Integer.valueOf(getX());

            case Y:
                return Integer.valueOf(getY());

            case V_X:
                return Double.valueOf(getV_x());

            case V_Y:
                return Double.valueOf(getV_y());

        }
        throw new IllegalStateException();
    }

    /**
     * Returns true if field corresponding to fieldID is set (has been assigned a value) and false
     * otherwise
     */
    public boolean isSet(_Fields field) {
        if (field == null) {
            throw new IllegalArgumentException();
        }

        switch (field) {
            case ID:
                return isSetId();
            case X:
                return isSetX();
            case Y:
                return isSetY();
            case V_X:
                return isSetV_x();
            case V_Y:
                return isSetV_y();
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
        if (that == null)
            return false;
        if (that instanceof TPlayer)
            return this.equals((TPlayer) that);
        return false;
    }

    public boolean equals(TPlayer that) {
        if (that == null)
            return false;

        boolean this_present_id = true;
        boolean that_present_id = true;
        if (this_present_id || that_present_id) {
            if (!(this_present_id && that_present_id))
                return false;
            if (this.id != that.id)
                return false;
        }

        boolean this_present_x = true;
        boolean that_present_x = true;
        if (this_present_x || that_present_x) {
            if (!(this_present_x && that_present_x))
                return false;
            if (this.x != that.x)
                return false;
        }

        boolean this_present_y = true;
        boolean that_present_y = true;
        if (this_present_y || that_present_y) {
            if (!(this_present_y && that_present_y))
                return false;
            if (this.y != that.y)
                return false;
        }

        boolean this_present_v_x = true;
        boolean that_present_v_x = true;
        if (this_present_v_x || that_present_v_x) {
            if (!(this_present_v_x && that_present_v_x))
                return false;
            if (this.v_x != that.v_x)
                return false;
        }

        boolean this_present_v_y = true;
        boolean that_present_v_y = true;
        if (this_present_v_y || that_present_v_y) {
            if (!(this_present_v_y && that_present_v_y))
                return false;
            if (this.v_y != that.v_y)
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public int compareTo(TPlayer other) {
        if (!getClass().equals(other.getClass())) {
            return getClass().getName().compareTo(other.getClass().getName());
        }

        int lastComparison = 0;
        TPlayer typedOther = (TPlayer) other;

        lastComparison = Boolean.valueOf(isSetId()).compareTo(typedOther.isSetId());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetId()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.id, typedOther.id);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
        lastComparison = Boolean.valueOf(isSetX()).compareTo(typedOther.isSetX());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetX()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.x, typedOther.x);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
        lastComparison = Boolean.valueOf(isSetY()).compareTo(typedOther.isSetY());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetY()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.y, typedOther.y);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
        lastComparison = Boolean.valueOf(isSetV_x()).compareTo(typedOther.isSetV_x());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetV_x()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.v_x, typedOther.v_x);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
        lastComparison = Boolean.valueOf(isSetV_y()).compareTo(typedOther.isSetV_y());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetV_y()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.v_y, typedOther.v_y);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
        return 0;
    }

    public _Fields fieldForId(int fieldId) {
        return _Fields.findByThriftId(fieldId);
    }

    public void read(org.apache.thrift.protocol.TProtocol iprot)
            throws org.apache.thrift.TException {
        schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot)
            throws org.apache.thrift.TException {
        schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TPlayer(");
        boolean first = true;

        sb.append("id:");
        sb.append(this.id);
        first = false;
        if (!first)
            sb.append(", ");
        sb.append("x:");
        sb.append(this.x);
        first = false;
        if (!first)
            sb.append(", ");
        sb.append("y:");
        sb.append(this.y);
        first = false;
        if (!first)
            sb.append(", ");
        sb.append("v_x:");
        sb.append(this.v_x);
        first = false;
        if (!first)
            sb.append(", ");
        sb.append("v_y:");
        sb.append(this.v_y);
        first = false;
        sb.append(")");
        return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
        // check for required fields
        // check for sub-struct validity
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        try {
            write(new org.apache.thrift.protocol.TCompactProtocol(
                    new org.apache.thrift.transport.TIOStreamTransport(out)));
        } catch (org.apache.thrift.TException te) {
            throw new java.io.IOException(te);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException,
            ClassNotFoundException {
        try {
            // it doesn't seem like you should have to do this, but java serialization is wacky, and
            // doesn't call the default constructor.
            __isset_bitfield = 0;
            read(new org.apache.thrift.protocol.TCompactProtocol(
                    new org.apache.thrift.transport.TIOStreamTransport(in)));
        } catch (org.apache.thrift.TException te) {
            throw new java.io.IOException(te);
        }
    }

    private static class TPlayerStandardSchemeFactory implements SchemeFactory {
        public TPlayerStandardScheme getScheme() {
            return new TPlayerStandardScheme();
        }
    }

    private static class TPlayerStandardScheme extends StandardScheme<TPlayer> {

        public void read(org.apache.thrift.protocol.TProtocol iprot, TPlayer struct)
                throws org.apache.thrift.TException {
            org.apache.thrift.protocol.TField schemeField;
            iprot.readStructBegin();
            while (true) {
                schemeField = iprot.readFieldBegin();
                if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
                    break;
                }
                switch (schemeField.id) {
                    case 1: // ID
                        if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
                            struct.id = iprot.readI64();
                            struct.setIdIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    case 2: // X
                        if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
                            struct.x = iprot.readI32();
                            struct.setXIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    case 3: // Y
                        if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
                            struct.y = iprot.readI32();
                            struct.setYIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    case 4: // V_X
                        if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
                            struct.v_x = iprot.readDouble();
                            struct.setV_xIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    case 5: // V_Y
                        if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
                            struct.v_y = iprot.readDouble();
                            struct.setV_yIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    default:
                        org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                }
                iprot.readFieldEnd();
            }
            iprot.readStructEnd();

            // check for required fields of primitive type, which can't be checked in the validate
            // method
            struct.validate();
        }

        public void write(org.apache.thrift.protocol.TProtocol oprot, TPlayer struct)
                throws org.apache.thrift.TException {
            struct.validate();

            oprot.writeStructBegin(STRUCT_DESC);
            oprot.writeFieldBegin(ID_FIELD_DESC);
            oprot.writeI64(struct.id);
            oprot.writeFieldEnd();
            oprot.writeFieldBegin(X_FIELD_DESC);
            oprot.writeI32(struct.x);
            oprot.writeFieldEnd();
            oprot.writeFieldBegin(Y_FIELD_DESC);
            oprot.writeI32(struct.y);
            oprot.writeFieldEnd();
            oprot.writeFieldBegin(V_X_FIELD_DESC);
            oprot.writeDouble(struct.v_x);
            oprot.writeFieldEnd();
            oprot.writeFieldBegin(V_Y_FIELD_DESC);
            oprot.writeDouble(struct.v_y);
            oprot.writeFieldEnd();
            oprot.writeFieldStop();
            oprot.writeStructEnd();
        }

    }

    private static class TPlayerTupleSchemeFactory implements SchemeFactory {
        public TPlayerTupleScheme getScheme() {
            return new TPlayerTupleScheme();
        }
    }

    private static class TPlayerTupleScheme extends TupleScheme<TPlayer> {

        @Override
        public void write(org.apache.thrift.protocol.TProtocol prot, TPlayer struct)
                throws org.apache.thrift.TException {
            TTupleProtocol oprot = (TTupleProtocol) prot;
            BitSet optionals = new BitSet();
            if (struct.isSetId()) {
                optionals.set(0);
            }
            if (struct.isSetX()) {
                optionals.set(1);
            }
            if (struct.isSetY()) {
                optionals.set(2);
            }
            if (struct.isSetV_x()) {
                optionals.set(3);
            }
            if (struct.isSetV_y()) {
                optionals.set(4);
            }
            oprot.writeBitSet(optionals, 5);
            if (struct.isSetId()) {
                oprot.writeI64(struct.id);
            }
            if (struct.isSetX()) {
                oprot.writeI32(struct.x);
            }
            if (struct.isSetY()) {
                oprot.writeI32(struct.y);
            }
            if (struct.isSetV_x()) {
                oprot.writeDouble(struct.v_x);
            }
            if (struct.isSetV_y()) {
                oprot.writeDouble(struct.v_y);
            }
        }

        @Override
        public void read(org.apache.thrift.protocol.TProtocol prot, TPlayer struct)
                throws org.apache.thrift.TException {
            TTupleProtocol iprot = (TTupleProtocol) prot;
            BitSet incoming = iprot.readBitSet(5);
            if (incoming.get(0)) {
                struct.id = iprot.readI64();
                struct.setIdIsSet(true);
            }
            if (incoming.get(1)) {
                struct.x = iprot.readI32();
                struct.setXIsSet(true);
            }
            if (incoming.get(2)) {
                struct.y = iprot.readI32();
                struct.setYIsSet(true);
            }
            if (incoming.get(3)) {
                struct.v_x = iprot.readDouble();
                struct.setV_xIsSet(true);
            }
            if (incoming.get(4)) {
                struct.v_y = iprot.readDouble();
                struct.setV_yIsSet(true);
            }
        }
    }

}
