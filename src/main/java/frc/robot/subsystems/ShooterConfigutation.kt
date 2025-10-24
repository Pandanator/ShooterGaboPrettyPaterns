package frc.robot.subsystems

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Current
import frc.robot.utils.RotationalDirection

data class ShooterConfiguration(
    val motorControllerId: Int,
    val motorFollowerId: Int,
    val motorDirection: RotationalDirection,
    val motorCurrentLimit: Current,
    val neutralMode: IdleMode
)

val shooterConfiguration = ShooterConfiguration(
    //SHOOTER 1
    //motorControllerId = 12,
    //motorFollowerId = 6,

    //SHOOTER 2
    motorControllerId = 52,
    motorFollowerId = 32,

    motorDirection = RotationalDirection.Clockwise,
    motorCurrentLimit = Units.Amps.of(40.0),
    neutralMode = IdleMode.kBrake
)

