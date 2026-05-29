package com.example.employeebackend.attendance.controller;

import com.example.employeebackend.attendance.entity.Holiday;
import com.example.employeebackend.attendance.service.HolidayService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController

@RequestMapping("/api/holidays")
@CrossOrigin(origins = "http://localhost:4200")

public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    /* GET → All holidays (used by calendar hover) */
    @GetMapping
    public List<Holiday> getAllHolidays() {
        return holidayService.getAllHolidays();
    }

    /* POST → Add holiday */
    @PostMapping
    public Holiday addHoliday(@RequestBody Holiday holiday) {
        return holidayService.addHoliday(holiday);
    }

    /* GET → Check if date is holiday */
    @GetMapping("/check")
    public boolean isHoliday(@RequestParam String date) {
        return holidayService.isHoliday(LocalDate.parse(date));
    }
}