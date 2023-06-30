package com.valencia.edfform;

import entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.text.SimpleDateFormat;
@WebServlet(name = "StudentServlet", urlPatterns = {"/submit"})
public class StudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
            EntityManager em = emf.createEntityManager();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            //Parsing student entity
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setStudentName(request.getParameter("studentName"));
            studentEntity.setStudentVid(request.getParameter("studentVid"));
            studentEntity.setStudentDepartment(request.getParameter("studentDepartment"));
            studentEntity.setStudentPosition(request.getParameter("studentPosition"));
            studentEntity.setStudentStartDate(new Date(format.parse(request.getParameter("studentStartDate")).getTime()));
            studentEntity.setStudentIndexNum(request.getParameter("studentIndexNum"));
            studentEntity.setStudentAccountNum(request.getParameter("studentAccountNum"));
            studentEntity.setStudentPurpose(request.getParameter("studentPurpose"));
            studentEntity.setStudentCollegeBenefit(request.getParameter("studentCollegeBenefit"));
            studentEntity.setStudentSign(request.getParameter("studentSign"));
            studentEntity.setStudentSignDate(new Date(format.parse(request.getParameter("studentSignDate")).getTime()));

            //Parsing college entity
            CollegeEntity collegeEntity = new CollegeEntity();
            collegeEntity.setCollegeCourseName(request.getParameter("collegeCourseName"));
            collegeEntity.setCollegeCourseNumber(request.getParameter("collegeCourseNumber"));
            collegeEntity.setCollegeCreditHours(new BigDecimal(request.getParameter("collegeCreditHours")));
            collegeEntity.setCollegeDegreeTitle(request.getParameter("collegeDegreeTitle"));
            collegeEntity.setCollegeName(request.getParameter("collegeName"));
            collegeEntity.setCollegeCoursestartDate(new Date(format.parse(request.getParameter("collegeCoursestartDate")).getTime()));
            collegeEntity.setCollegeCourseendDate(new Date(format.parse(request.getParameter("collegeCourseendDate")).getTime()));
            collegeEntity.setCollegePartofDegree(request.getParameter("collegePartofDegree"));
            collegeEntity.setCollegeDegreeType(request.getParameter("collegeDegreeType"));
            String collegeTuitionCostStr = request.getParameter("collegeTuitionCost");
            BigDecimal collegeTuitionCost = new BigDecimal(collegeTuitionCostStr);
            collegeEntity.setCollegeTuitionCost(collegeTuitionCost);
            collegeEntity.setCollegeFundsRequested(request.getParameter("collegeFundsRequested"));
            collegeEntity.setCollegeTuitionType(request.getParameter("collegeTuitionType"));

            //Parsing employer entity
            EmployerEntity employerEntity = new EmployerEntity();
            employerEntity.setEmployerTrainingTitle(request.getParameter("employerTrainingTitle"));
            employerEntity.setEmployerHostOrg(request.getParameter("employerHostOrg"));
            employerEntity.setEmployerLocation(request.getParameter("employerLocation"));
            String employerLocationStartDateStr = request.getParameter("employerLocationstartDate");
            if (employerLocationStartDateStr != null && !employerLocationStartDateStr.isEmpty()) {
                Date employerLocationStartDate = format.parse(employerLocationStartDateStr);
                employerEntity.setEmployerLocationstartDate(new java.sql.Date(employerLocationStartDate.getTime()));
            } else {
                employerEntity.setEmployerLocationstartDate(null);
            }
            String employerLocationEndDateStr = request.getParameter("employerLocationendDate");
            if (employerLocationEndDateStr != null && !employerLocationEndDateStr.isEmpty()) {
                Date employerLocationEndDate = format.parse(employerLocationEndDateStr);
                employerEntity.setEmployerLocationendDate(new java.sql.Date(employerLocationEndDate.getTime()));
            } else {
                employerEntity.setEmployerLocationendDate(null);
            }
            String employerRegistrationCostStr = request.getParameter("employerRegistrationCost");
            BigDecimal employerRegistrationCost;
            if (employerRegistrationCostStr != null && !employerRegistrationCostStr.isEmpty()) {
                try {
                    employerRegistrationCost = new BigDecimal(employerRegistrationCostStr);
                } catch (NumberFormatException e) {
                    // Handle the case when the input string is not a valid number
                    // You can set a default value or display an error message
                    employerRegistrationCost = BigDecimal.ZERO; // Default value
                }
            } else {
                employerRegistrationCost = null; // or set it to a default value if applicable
            }
            employerEntity.setEmployerRegistrationCost(employerRegistrationCost);
            employerEntity.setEmployerRegistrationPay(request.getParameter("employerRegistrationPay"));

            //Parsing supervisor entity
            SupervisorEntity supervisorEntity = new SupervisorEntity();
            supervisorEntity.setStudentId(studentEntity.getId());
            supervisorEntity.setSupervisorName(null);
            supervisorEntity.setSupervisorExplain(null);
            supervisorEntity.setSupervisorSign(null);
            supervisorEntity.setSupervisorSignDate(null);

            //Parsing odhr entity
            OdhrEntity odhrEntity = new OdhrEntity();
            odhrEntity.setStudentId(studentEntity.getId());
            odhrEntity.setOdhrFunds(null);
            odhrEntity.setOdhrEligibility(null);
            odhrEntity.setOdhrComment(null);
            odhrEntity.setOdhrSign(null);
            odhrEntity.setOdhrName(null);
            odhrEntity.setOdhrSignDate(null);

            em.getTransaction().begin();
            em.persist(studentEntity); //This has to be persisted first
            System.out.println("Student ID: " + studentEntity.getId());
            supervisorEntity.setStudentId(studentEntity.getId());
            odhrEntity.setStudentId(studentEntity.getId());
            em.persist(collegeEntity);
            em.persist(employerEntity);
            em.persist(odhrEntity);
            em.persist(supervisorEntity);

            em.getTransaction().commit();

            em.close();
            emf.close();

            response.sendRedirect("success.jsp"); // redirect to success page

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Optionally handle GET requests
    }
}
