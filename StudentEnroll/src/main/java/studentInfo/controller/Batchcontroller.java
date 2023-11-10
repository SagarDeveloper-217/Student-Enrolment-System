package studentInfo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import studentInfo.entities.Batch;
import studentInfo.entities.Student;
import studentInfo.repositories.BatchRepo;

@RestController
public class Batchcontroller {

	@Autowired
	BatchRepo batchRepo;

	// List All Batches
	@GetMapping("/allbatches")
	@Operation(summary = "Get All Batches", description = "Retrives List of Batches")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrived"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Batch> batch() {
		return batchRepo.findAll();
	}

	// 2
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addbatch")
	@Operation(summary = "Add Batch", description = "Add a batches ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Added"),
			@ApiResponse(responseCode = "400", description = "Bad request batch details is incorrect"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public Batch addNewBatch(@Valid @RequestBody Batch newbatch) {
		try {
			List<Batch> existingbatches = batchRepo.findAll();
			boolean exist = existingbatches.contains(newbatch);
			if (exist)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "timimgs in same day Already Present");
			batchRepo.save(newbatch);
			return newbatch;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatebatchbyid/{id}")
	@Operation(summary = "Update Batches", description = "Update Batches")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Updated"),
			@ApiResponse(responseCode = "404", description = "batch Id NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void updateOneBatch(@Valid @PathVariable("id") Integer id, @RequestBody Batch batch) {
		try {
			var optbatch = batchRepo.findById(id);
			if (optbatch.isPresent()) {
				Batch b = optbatch.get();
				b.setCode(batch.getCode());
				b.setStartDate(batch.getStartDate());
				b.setTime(batch.getTime());
				b.setDuration(batch.getDuration());
				b.setFee(batch.getFee());
				batchRepo.save(b);
			} else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch id Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deletebatchbyid/{id}")
	@Operation(summary = "Delete Batches", description = "Deletes Batches of giving Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Deleted"),
			@ApiResponse(responseCode = "404", description = "batch Id NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void deleteOneBatch(
			@Parameter(description = "Batch that is to be deleted", allowEmptyValue = false) @PathVariable("id") Integer id) {
		try {
			var batch = batchRepo.findById(id);
			if (batch.isPresent())
				batchRepo.deleteById(id);
			else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "batch with this id is Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 6
	@GetMapping("/currentrunningbatches")
	@Operation(summary = "Get Running Batches", description = "Retrives all the batches that are currently running")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "204", description = "NOT Content found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Batch> getPresentRunningBatches() {
		try {
			List<Batch> b = batchRepo.getRunningBatch(LocalDate.now());
			if (!b.isEmpty())
				return b;
			else {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Batches Not Found!");
			}
		} catch (ResponseStatusException ex) {
			throw ;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 7
	@GetMapping("/completedbatches")
	@Operation(summary = "Get Completed Batches", description = "Retrives all the batches that are completed")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "204", description = "NOT Content completed batches found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Batch> getcompletedBatches() {
		try {
			List<Batch> b = batchRepo.getCompletedBatch(LocalDate.now());
			if (!b.isEmpty())
				return b;
			else {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Batches Not Found!");
			}
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 8
	@GetMapping("/liststudentsofrunningbatches")
	@Operation(summary = "Get Studnets in Running Batches", description = "Retrives all the Students that are in currently running batches")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "204", description = "NO Content current running batches Students"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Student> getCurrentRunningBatchesStudents() {
		try {
			List<Student> b = batchRepo.getRunningBatchStudent(LocalDate.now());
			if (!b.isEmpty())
				return b;
			else {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Students Not Found!");
			}
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 11
	@GetMapping("/getbatchesbycode/{code}")
	@Operation(summary = "Get Batches by course code", description = "Retrives all the batches of the given course")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	public List<Batch> getBatchesByCode(@PathVariable("code") String code) {
		try {
			List<Batch> b = batchRepo.findByCode(code);
			if (!b.isEmpty())
				return b;
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "batches Not Found!");
			}
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 13
	@GetMapping("/batchesstartedb/w2dates")
	@Operation(summary = "Get batches btw two dates", description = "Retrives all the batches that are started between given two given dates")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	public List<Batch> getBatchesbetweentwodates(@RequestParam("startdate") LocalDate startdate,
			@RequestParam("enddate") LocalDate enddate) {
		try {
			List<Batch> b = batchRepo.getRunningBatchBetweenDates(startdate, enddate);
			if (!b.isEmpty())
				return b;
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "batches Not Found!");
			}
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

}
