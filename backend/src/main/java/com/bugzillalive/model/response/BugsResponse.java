package com.bugzillalive.model.response;

import com.bugzillalive.model.bug.Bug;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BugsResponse {
	public List<Bug> bugs;
}
