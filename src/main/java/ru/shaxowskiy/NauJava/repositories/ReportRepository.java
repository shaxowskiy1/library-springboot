package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shaxowskiy.NauJava.models.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
}
