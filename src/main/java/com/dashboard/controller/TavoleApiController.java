/*
package com.dashboard.controller;

import com.dashboard.service.ExcelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tavole")
public class TavoleApiController {

    private final ExcelService excel;
    public TavoleApiController(ExcelService excel) { this.excel = excel; }

    // Esempi:
    // GET /api/tavole/andamento  → legge Tavole_andamento_economia_agricola2025.xls
    // GET /api/tavole/unita      → legge Unità agricole ....xlsx
    @GetMapping("/{which}")
    public List<Map<String,String>> read(@PathVariable String which) throws Exception {
        return excel.readFirstSheet(which);
    }
}
*/

