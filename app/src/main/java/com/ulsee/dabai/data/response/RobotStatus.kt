package com.ulsee.dabai.data.response

data class RobotStatus(
    val motion: RobotStatusMotion
)

data class RobotStatusMotion (
    val pose: Array<Double>
)

/*
{
  "battery": {
    "charging": false,
    "charging_current": 0,
    "consumption_current": 0,
    "electricity": 88,
    "status_code": 1,
    "voltage": 0
  },
  "clean_machine": {
    "center_sweep_motor": {
      "id": 0,
      "speed": 0,
      "status_code": 1
    },
    "dustbox": false,
    "left_sweep_motor": {
      "id": 0,
      "speed": 0,
      "status_code": 1
    },
    "lifting_motor": {
      "position": 2,
      "status_code": 1
    },
    "right_sweep_motor": {
      "id": 0,
      "speed": 0,
      "status_code": 1
    },
    "roll_motor": {
      "speed": 0,
      "status_code": 1
    },
    "status_code": 1,
    "vacuum_motor": {
      "speed": 0,
      "status_code": 1
    }
  },
  "front_camera": {
    "status_code": 0
  },
  "gyrometer": {
    "status_code": 1
  },
  "internet": {
    "connection": false,
    "remaining_data": 0,
    "remaining_money": 0
  },
  "laser": {
    "status_code": 0
  },
  "left_wheel": {
    "status_code": 0
  },
  "mainboard": {
    "status_code": 0
  },
  "motion": {
    "located": false,
    "map_floor": 0,
    "map_id": 0,
    "pause_code": 1,
    "pose": [
      0,
      0,
      0,
      0,
      0,
      0
    ],
    "taking_elevator": false,
    "velocity": [
      0,
      0,
      0,
      0,
      0,
      0
    ]
  },
  "rear_camera": {
    "status_code": 0
  },
  "rgbd_camera": {
    "status_code": 0
  },
  "right_wheel": {
    "status_code": 0
  },
  "task": {
    "error_code": 0,
    "mode": 4,
    "task_id": 0,
    "task_progress": 100
  },
  "ultrasonic": [
    {
      "id": 0,
      "status_code": 1
    },
    {
      "id": 1,
      "status_code": 1
    },
    {
      "id": 2,
      "status_code": 1
    },
    {
      "id": 3,
      "status_code": 1
    },
    {
      "id": 4,
      "status_code": 1
    },
    {
      "id": 5,
      "status_code": 1
    },
    {
      "id": 6,
      "status_code": 1
    },
    {
      "id": 7,
      "status_code": 1
    }
  ],
  "robot_id": "88",
  "robot_mark": "B00088",
  "loadavg": [
    2.22216796875,
    2.6025390625,
    1.6630859375
  ],
  "totalmem": "7.70",
  "freemem": "6.14",
  "cpu_percent": "0.28"
}
 */