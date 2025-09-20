package frc.robot.subsystems

import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.math.MathUtil
import frc.robot.utils.RotationalDirection
import edu.wpi.first.units.Units.Amps
import edu.wpi.first.units.Units.Volts
import edu.wpi.first.units.measure.Voltage
import edu.wpi.first.wpilibj2.command.SubsystemBase

class Shooter(private val config: ShooterConfiguration) : SubsystemBase() {
    private val motorController = SparkMax(config.motorControllerId, SparkLowLevel.MotorType.kBrushless)
    private val motorFollower = SparkMax(config.motorFollowerId, SparkLowLevel.MotorType.kBrushless)
    private var appliedVoltage: Voltage = Volts.of(0.0)

    override fun periodic() {
        val clampedVoltage = MathUtil.clamp(
            appliedVoltage.`in`(Volts),
            Volts.of(-12.0).`in`(Volts),
            Volts.of(12.0).`in`(Volts)
        )
        motorController.setVoltage(clampedVoltage)
    }

    fun setVoltage(voltage: Voltage) {
        appliedVoltage = voltage
    }

    fun increaseVoltage() {
        appliedVoltage += Volts.of(1.0)
    }

    fun decreaseVoltage() {
        appliedVoltage -= Volts.of(1.0)
    }

    fun stopMotor() {
        appliedVoltage = Volts.of(0.0)
    }

    init {
        configureMotorInterface()
    }

    private fun configureMotorInterface() {
        val sparkConfig = SparkMaxConfig()
        val followerConfig = SparkMaxConfig()

        with(sparkConfig) {
            idleMode(config.neutralMode).inverted(
                config.motorDirection.opposite() == RotationalDirection.CounterClockwise
            ).smartCurrentLimit(config.motorCurrentLimit.`in`(Amps).toInt())
        }
        followerConfig.apply(sparkConfig).follow(config.motorControllerId, false)

        motorController.clearFaults()
        motorFollower.clearFaults()

        motorController.configure(
            sparkConfig,
            SparkBase.ResetMode.kResetSafeParameters,
            SparkBase.PersistMode.kNoPersistParameters
        )
        motorFollower.configure(
            followerConfig,
            SparkBase.ResetMode.kResetSafeParameters,
            SparkBase.PersistMode.kPersistParameters
        )
    }
}
