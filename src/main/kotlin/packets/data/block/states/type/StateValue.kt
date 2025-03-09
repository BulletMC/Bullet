package com.aznos.packets.data.block.states.type

import com.aznos.packets.data.block.BlockFace
import com.aznos.packets.data.block.states.enums.*

enum class StateValue(
    val named: String, val dataClass: Class<*>, val parser: (String) -> Any
) {

    AGE("age", Int::class.java, String::toInt),
    ATTACHED("attached", Boolean::class.java, String::toBoolean),
    ATTACHMENT("attachment", Attachment::class.java, Attachment::valueOf),
    AXIS("axis", Axis::class.java, Axis::valueOf),
    BERRIES("berries", Boolean::class.java, String::toBoolean),
    BITES("bites", Int::class.java, String::toInt),
    BLOOM("bloom", Int::class.java, String::toInt),
    BOTTOM("bottom", Boolean::class.java, String::toBoolean),
    CANDLES("candles", Int::class.java, String::toInt),
    CAN_SUMMON("can_summon", Boolean::class.java, String::toBoolean),
    CHARGES("charges", Int::class.java, String::toInt),
    CONDITIONAL("conditional", Boolean::class.java, String::toBoolean),
    CRACKED("cracked", Boolean::class.java, String::toBoolean),
    CRAFTING("crafting", Boolean::class.java, String::toBoolean),
    DELAY("delay", Int::class.java, String::toInt),
    DISARMED("disarmed", Boolean::class.java, String::toBoolean),
    DISTANCE("distance", Int::class.java, String::toInt),
    DOWN("down", Boolean::class.java, String::toBoolean),
    DRAG("drag", Boolean::class.java, String::toBoolean),
    DUSTED("dusted", Int::class.java, String::toInt),
    EAST("east", East::class.java, East::valueOf),
    EGGS("eggs", Int::class.java, String::toInt),
    ENABLED("enabled", Boolean::class.java, String::toBoolean),
    EXTENDED("extended", Boolean::class.java, String::toBoolean),
    EYE("eye", Boolean::class.java, String::toBoolean),
    FACE("face", Face::class.java, Face::valueOf),
    FACING("facing", BlockFace::class.java, BlockFace::valueOf),
    FLOWER_AMOUNT("flower_amount", Int::class.java, String::toInt),
    HALF("half", Half::class.java, Half::valueOf),
    HANGING("hanging", Boolean::class.java, String::toBoolean),
    HAS_BOOK("has_book", Boolean::class.java, String::toBoolean),
    HAS_BOTTLE_0("has_bottle_0", Boolean::class.java, String::toBoolean),
    HAS_BOTTLE_1("has_bottle_1", Boolean::class.java, String::toBoolean),
    HAS_BOTTLE_2("has_bottle_2", Boolean::class.java, String::toBoolean),
    HAS_RECORD("has_record", Boolean::class.java, String::toBoolean),
    HATCH("hatch", Int::class.java, String::toInt),
    HINGE("hinge", Hinge::class.java, Hinge::valueOf),
    HONEY_LEVEL("honey_level", Int::class.java, String::toInt),
    IN_WALL("in_wall", Boolean::class.java, String::toBoolean),
    INSTRUMENT("instrument", Instrument::class.java, Instrument::valueOf),
    INVERTED("inverted", Boolean::class.java, String::toBoolean),
    LAYERS("layers", Int::class.java, String::toInt),
    LEAVES("leaves", Leaves::class.java, Leaves::valueOf),
    LEVEL("level", Int::class.java, String::toInt),
    LIT("lit", Boolean::class.java, String::toBoolean),
    TIP("tip", Boolean::class.java, String::toBoolean),
    LOCKED("locked", Boolean::class.java, String::toBoolean),
    MODE("mode", Mode::class.java, Mode::valueOf),
    MOISTURE("moisture", Int::class.java, String::toInt),
    NORTH("north", North::class.java, North::valueOf),
    NOTE("note", Int::class.java, String::toInt),
    OCCUPIED("occupied", Boolean::class.java, String::toBoolean),
    OMINOUS("ominous", Boolean::class.java, String::toBoolean),
    OPEN("open", Boolean::class.java, String::toBoolean),
    ORIENTATION("orientation", Orientation::class.java, Orientation::valueOf),
    PART("part", Part::class.java, Part::valueOf),
    PERSISTENT("persistent", Boolean::class.java, String::toBoolean),
    PICKLES("pickles", Int::class.java, String::toInt),
    POWER("power", Int::class.java, String::toInt),
    POWERED("powered", Boolean::class.java, String::toBoolean),
    ROTATION("rotation", Int::class.java, String::toInt),
    SCULK_SENSOR_PHASE("sculk_sensor_phase",SculkSensorPhase::class.java, SculkSensorPhase::valueOf),
    SHAPE("shape", Shape::class.java, Shape::valueOf),
    SHORT("short", Boolean::class.java, String::toBoolean),
    SHRIEKING("shrieking", Boolean::class.java, String::toBoolean),
    SIGNAL_FIRE("signal_fire", Boolean::class.java, String::toBoolean),
    SLOT_0_OCCUPIED("slot_0_occupied", Boolean::class.java, String::toBoolean),
    SLOT_1_OCCUPIED("slot_1_occupied", Boolean::class.java, String::toBoolean),
    SLOT_2_OCCUPIED("slot_2_occupied", Boolean::class.java, String::toBoolean),
    SLOT_3_OCCUPIED("slot_3_occupied", Boolean::class.java, String::toBoolean),
    SLOT_4_OCCUPIED("slot_4_occupied", Boolean::class.java, String::toBoolean),
    SLOT_5_OCCUPIED("slot_5_occupied", Boolean::class.java, String::toBoolean),
    SNOWY("snowy", Boolean::class.java, String::toBoolean),
    STAGE("stage", Int::class.java, String::toInt),
    SOUTH("south", South::class.java, South::valueOf),
    THICKNESS("thickness", Thickness::class.java, Thickness::valueOf),
    TILT("tilt", Tilt::class.java, Tilt::valueOf),
    TRIAL_SPAWNER_STATE("trial_spawner_state", TrialSpawnerState::class.java, TrialSpawnerState::valueOf),
    TRIGGERED("triggered", Boolean::class.java, String::toBoolean),
    TYPE("type", Type::class.java, Type::valueOf),
    UNSTABLE("unstable", Boolean::class.java, String::toBoolean),
    UP("up", Boolean::class.java, String::toBoolean),
    VAULT_STATE("vault_state", VaultState::class.java, VaultState::valueOf),
    VERTICAL_DIRECTION("vertical_direction", VerticalDirection::class.java, VerticalDirection::valueOf),
    WATERLOGGED("waterlogged", Boolean::class.java, String::toBoolean),
    WEST("west", West::class.java, West::valueOf),
    ACTIVE("active", Boolean::class.java, String::toBoolean),
    NATURAL("natural", Boolean::class.java, String::toBoolean);

    fun parse(input: String): Any {
        return parser(input)
    }

    companion object {
        val NAME_INDEX = entries.associateBy { it.named }

        fun byName(name: String): StateValue? = NAME_INDEX[name]
    }

}