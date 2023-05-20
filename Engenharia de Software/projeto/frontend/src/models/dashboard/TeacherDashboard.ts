import TeacherStudentStats from '@/models/dashboard/TeacherStudentStats';
import QuizStats from '@/models/dashboard/QuizStats';
import QuestionStats from '@/models/dashboard/QuestionStats';

export default class TeacherDashboard {
  id!: number;
  studentStats: TeacherStudentStats[] = [];
  quizStats!: QuizStats[];
  questionStats!: QuestionStats[];
  courseExecutionYear!: number;

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.courseExecutionYear = jsonObj.courseExecutionYear;

      if (jsonObj.studentStats) {
        this.studentStats = jsonObj.studentStats.map(
          (studentStat: TeacherStudentStats) =>
            new TeacherStudentStats(studentStat)
        );
      }

      if (jsonObj.quizStats) {
        this.quizStats = jsonObj.quizStats.map(
          (quizStats: QuizStats) => new QuizStats(quizStats)
        );
      }

      if (jsonObj.questionStats) {
        this.questionStats = jsonObj.questionStats.map(
          (questionStats: QuestionStats) => new QuestionStats(questionStats)
        );
      }

    }
  }
}
