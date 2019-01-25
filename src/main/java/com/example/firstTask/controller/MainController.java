package com.example.firstTask.controller;

import com.example.firstTask.thread.UtilFindMaxValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class MainController {
	
	
	private List<String> listEl = new ArrayList<>();
	public static List<Integer> results = new ArrayList<>();
	public static  volatile  long  time = 0;
	
	@GetMapping(value = "")
	public String getMain() {
		return "main";
	}
	
	@PostMapping(value = "/generate")
	public String Generate(@RequestParam(name = "elements", required = false) int elements) {
		listEl.clear();
		Random random = new Random();
		for (int i = 0; i < elements; i++) {
			
			listEl.add(String.valueOf(random.nextInt(elements)));
		}
		
		return "redirect:/";
	}
	
	@PostMapping(value = "/write_to_file")
	public String writeToFile(@RequestParam(name = "file", required = false) String file) {
		
		try (final FileWriter writer = new FileWriter(file)) {
			
			for (int i = 0; i < listEl.size(); i++) {
				writer.write(listEl.get(i));
				writer.write(System.lineSeparator());
			}
		} catch (IOException e) {
		}
		return "redirect:/";
	}
	
	@PostMapping(value = "/read_from_file")
	public String readFromFile(@RequestParam(name = "fromFile", required = false) String fromFile) {
		listEl.clear();
		try (final FileReader reader = new FileReader(fromFile)) {
			BufferedReader bufferedReader = new BufferedReader(reader);
			String s;
			while ((s = bufferedReader.readLine()) != null) {
				
				listEl.add(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}
	
	@GetMapping(value = "/find_max")
	public String findMax(Model model) {
		long startTime = System.currentTimeMillis();
		int min = 0;
		int max;
		for (int i = 0; i < listEl.size(); i++) {
			max = Integer.parseInt(listEl.get(i));
			if (min < max) {
				min = max;
			}
		}
		long timeSpent = System.currentTimeMillis() - startTime;
		model.addAttribute("max", String.valueOf(min));
		model.addAttribute("time", String.valueOf(String.valueOf(timeSpent)));
		return "result";
	}
	
	@PostMapping(value = "/find_multi_max")
	public String findMultiMax(@RequestParam(name = "threads", required = false) String threads, Model model) throws InterruptedException {
		 results.clear();
		 
		int threadsEnd = Integer.parseInt(threads);
		int minSize = 0;
		int maxSize = listEl.size() / threadsEnd;
		
		for (int i = 0; i < threadsEnd; i++) {
			
			if (maxSize >= listEl.size()) {
				break;
			}
			
			new Thread(new UtilFindMaxValue(minSize, maxSize, listEl)).start();
			
		
			minSize += maxSize;
			maxSize += maxSize;
		}
		int resultMax = 0;
		
		 Thread.sleep(30000);
		for (int i = 0; i < results.size(); i++) {
			if (resultMax < results.get(i)) {
				resultMax = results.get(i);
			}
		}
		model.addAttribute("max", String.valueOf(resultMax));
		model.addAttribute("time", String.valueOf(String.valueOf(time)));
		return "result";
	}
}
