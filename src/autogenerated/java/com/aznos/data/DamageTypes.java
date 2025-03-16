package com.aznos.data;

import java.lang.String;
import org.jetbrains.annotations.NotNull;

public enum DamageTypes {
  THROWN("thrown"),

  GENERIC_KILL("generic_kill"),

  UNATTRIBUTED_FIREBALL("unattributed_fireball"),

  BAD_RESPAWN_POINT("bad_respawn_point"),

  FALLING_ANVIL("falling_anvil"),

  FIREWORKS("fireworks"),

  OUT_OF_WORLD("out_of_world"),

  SONIC_BOOM("sonic_boom"),

  OUTSIDE_BORDER("outside_border"),

  ENDER_PEARL("ender_pearl"),

  MACE_SMASH("mace_smash"),

  LAVA("lava"),

  STARVE("starve"),

  CRAMMING("cramming"),

  DRY_OUT("dry_out"),

  CACTUS("cactus"),

  WITHER("wither"),

  MOB_ATTACK("mob_attack"),

  FLY_INTO_WALL("fly_into_wall"),

  WITHER_SKULL("wither_skull"),

  PLAYER_ATTACK("player_attack"),

  GENERIC("generic"),

  TRIDENT("trident"),

  DROWN("drown"),

  THORNS("thorns"),

  MOB_PROJECTILE("mob_projectile"),

  STALAGMITE("stalagmite"),

  HOT_FLOOR("hot_floor"),

  FALLING_STALACTITE("falling_stalactite"),

  FALLING_BLOCK("falling_block"),

  WIND_CHARGE("wind_charge"),

  LIGHTNING_BOLT("lightning_bolt"),

  EXPLOSION("explosion"),

  STING("sting"),

  PLAYER_EXPLOSION("player_explosion"),

  DRAGON_BREATH("dragon_breath"),

  FALL("fall"),

  ARROW("arrow"),

  SPIT("spit"),

  IN_WALL("in_wall"),

  MOB_ATTACK_NO_AGGRO("mob_attack_no_aggro"),

  SWEET_BERRY_BUSH("sweet_berry_bush"),

  FIREBALL("fireball"),

  ON_FIRE("on_fire"),

  MAGIC("magic"),

  CAMPFIRE("campfire"),

  IN_FIRE("in_fire"),

  FREEZE("freeze"),

  INDIRECT_MAGIC("indirect_magic");

  public final String messageId;

  DamageTypes(@NotNull String messageId) {
    this.messageId = messageId;
  }
}
