<template>
  <div class="container">
    <h2>Statistics for this course execution</h2>
    <div v-if="teacherDashboard != null" class="stats-container">
      <div class="items">
        <div ref="totalStudents" class="icon-wrapper">
          <animated-number :number="studentStats.numStudents" />
        </div>
        <div class="project-name">
          <p>Number of Students</p>
        </div>
      </div>
      <div class="items">
        <div ref="totalStudents" class="icon-wrapper">
          <animated-number :number="studentStats.numMore75CorrectQuestions" />
        </div>
        <div class="project-name">
          <p>Number of Students who Solved >= 75% Questions</p>
        </div>
      </div>
      <div class="items">
        <div ref="totalStudents" class="icon-wrapper">
          <animated-number :number="studentStats.numAtLeast3Quizzes" />
        </div>
        <div class="project-name">
          <p>Number of Students who Solved >= 3 Quizzes</p>
        </div>
      </div>
    </div>
    <div v-if="teacherDashboard != null" class="stats-container">
      <div class="items">
        <div ref="totalStudents" class="icon-wrapper">
          <animated-number :number="quizStats.numQuizzes" />
        </div>
        <div class="project-name">
          <p>Number of Quizzes</p>
        </div>
      </div>
      <div class="items">
        <div ref="totalStudents" class="icon-wrapper">
          <animated-number :number="quizStats.numUniqueAnsweredQuizzes" />
        </div>
        <div class="project-name">
          <p>Number of Quizzes Solved (Unique)</p>
        </div>
      </div>
      <div class="items">
        <div ref="totalStudents" class="icon-wrapper">
          <animated-number :number="quizStats.averageQuizzesSolved" />
        </div>
        <div class="project-name">
          <p>Number of Quizzes Solved (Unique, Average Per Student)</p>
        </div>
      </div>
    </div>
    <div v-if="teacherDashboard != null" class="stats-container">
      <div class="items">
        <div ref="totalQuestions" class="icon-wrapper">
          <animated-number :number="questionStats.numAvailable" />
        </div>
        <div class="project-name">
          <p>Number of Questions</p>
        </div>
      </div>
      <div class="items">
        <div ref="answeredQuestionsUnique" class="icon-wrapper">
          <animated-number :number="questionStats.answeredQuestionsUnique" />
        </div>
        <div class="project-name">
          <p>Number of Questions Solved (Unique)</p>
        </div>
      </div>
      <div class="items">
        <div ref="averageQuestionsAnswered" class="icon-wrapper">
          <animated-number :number="questionStats.averageQuestionsAnswered" />
        </div>
        <div class="project-name">
          <p>
            Number of Questions Correctly Solved (Unique, Average Per Student)
          </p>
        </div>
      </div>
    </div>
    <h2>Comparison with previous course executions</h2>
    <div class="statschart-container">
      <div class="bar-chart">
        <StatsChart
          :attributes="studentStatsAttributes"
          :colors="colors"
          :labels="studentStatsLabels"
          :datasets="studentStatsDatasets"
        />
      </div>
      <div class="bar-chart">
        <StatsChart
          :attributes="quizStatsAttributes"
          :colors="colors"
          :labels="quizStatsLabels"
          :datasets="quizStatsDatasets"
        />
      </div>
      <div class="bar-chart">
        <StatsChart
          :attributes="questionStatsAttributes"
          :colors="colors"
          :labels="questionStatsLabels"
          :datasets="questionStatsDatasets"
      /></div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import AnimatedNumber from '@/components/AnimatedNumber.vue';
import TeacherDashboard from '@/models/dashboard/TeacherDashboard';
import QuizStats from '@/models/dashboard/QuizStats';
import QuestionStats from '@/models/dashboard/QuestionStats';
import TeacherStudentStats from '@/models/dashboard/TeacherStudentStats';
import StatsChart from '@/components/StatsChart.vue';

@Component({
  components: {
    StatsChart,
    AnimatedNumber,
  },
})
export default class TeacherStatsView extends Vue {
  @Prop() readonly dashboardId!: number;
  teacherDashboard: TeacherDashboard | null = null;
  studentStats!: TeacherStudentStats;
  quizStats!: QuizStats;
  questionStats!: QuestionStats;

  quizStatsLabels: string[] = [];
  quizStatsDatasets: number[][] = [[], [], []];
  quizStatsAttributes = [
    'Quizzes: Total Available',
    'Quizzes: Solved (Unique)',
    'Quizzes: Solved (Unique, Average per Student)',
  ];
  colors = ['#ff0000', '#00ff00', '#0000ff'];

  studentStatsLabels: string[] = [];
  studentStatsDatasets: number[][] = [[], [], []];
  studentStatsAttributes = [
    'Total Number of Students',
    'Students who solved >= 75% of Questions',
    'Students who solved >= 3 Quizzes',
  ];

  questionStatsLabels: string[] = [];
  questionStatsDatasets: number[][] = [[],[],[]];
  questionStatsAttributes =
  [
    'Questions: Total Available',
    'Questions: Solved (Unique)',
    'Questions: Correctly Solved (Unique, Average per Student)',
  ];

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.teacherDashboard = await RemoteServices.getTeacherDashboard();
      for(let stat of this.teacherDashboard.studentStats){
        if(stat.courseExecutionYear == this.teacherDashboard.courseExecutionYear){
          this.studentStats = stat;
          break;
        }
      }
      for (let stat of this.teacherDashboard.quizStats) {
        if (
          stat.courseExecutionYear == this.teacherDashboard.courseExecutionYear
        ) {
          this.quizStats = stat;
          break;
        }
      }
      for(let stat of this.teacherDashboard.questionStats){
        if(stat.courseExecutionYear == this.teacherDashboard.courseExecutionYear){
          this.questionStats = stat;
          break;
        }
      }

      for (let quizStat of this.teacherDashboard.quizStats.reverse()) {
        let label = quizStat.courseExecutionYear.toString();
        if (
          quizStat.courseExecutionYear == this.quizStats.courseExecutionYear
        ) {
          label = label.concat(' (current)');
        }
        this.quizStatsLabels.push(label);
        this.quizStatsDatasets[0].push(quizStat.numQuizzes);
        this.quizStatsDatasets[1].push(quizStat.averageQuizzesSolved);
        this.quizStatsDatasets[2].push(quizStat.numUniqueAnsweredQuizzes);
      }

      for (let studentStat of this.teacherDashboard.studentStats.reverse()) {
        let label = studentStat.courseExecutionYear.toString();
        if (
          studentStat.courseExecutionYear ==
          this.studentStats.courseExecutionYear
        ) {
          label = label.concat(' (current)');
        }
        this.studentStatsLabels.push(label);
        this.studentStatsDatasets[0].push(studentStat.numStudents);
        this.studentStatsDatasets[1].push(
          studentStat.numMore75CorrectQuestions
        );
        this.studentStatsDatasets[2].push(studentStat.numAtLeast3Quizzes);
      }

      for (let questionStat of this.teacherDashboard.questionStats) {
        let label = questionStat.courseExecutionYear.toString();
        if (questionStat.courseExecutionYear == this.questionStats.courseExecutionYear){
            label = label.concat(' (current)');
        }
        this.questionStatsLabels.push(label);
        this.questionStatsDatasets[0].push(questionStat.numAvailable);
        this.questionStatsDatasets[1].push(questionStat.averageQuestionsAnswered);
        this.questionStatsDatasets[2].push(questionStat.answeredQuestionsUnique);
      }

    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }
}
</script>

<style lang="scss" scoped>
//Not finished
.statschart-container {
  display: flex;
  justify-content: center;
  align-items: stretch;
  justify-items: stretch;
  align-content: center;
  height: 100%;
}
.stats-container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  align-items: stretch;
  align-content: center;
  height: 100%;

  .items {
    background-color: rgba(255, 255, 255, 0.75);
    color: #1976d2;
    border-radius: 5px;
    flex-basis: 25%;
    margin: 20px;
    cursor: pointer;
    transition: all 0.6s;
  }

  .bar-chart {
    background-color: rgba(255, 255, 255, 0.90);
    margin: 20px;
    height: 400px;
  }
}

.icon-wrapper,
.project-name {
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-wrapper {
  font-size: 100px;
  transform: translateY(0px);
  transition: all 0.6s;
}

.icon-wrapper {
  align-self: end;
}

.project-name {
  align-self: start;
}

.project-name p {
  font-size: 24px;
  font-weight: bold;
  letter-spacing: 2px;
  transform: translateY(0px);
  transition: all 0.5s;
}

.items:hover {
  border: 3px solid black;

  & .project-name p {
    transform: translateY(-10px);
  }

  & .icon-wrapper i {
    transform: translateY(5px);
  }
}
</style>
