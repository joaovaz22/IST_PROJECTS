<template>
  <Bar v-if="labels.length > 1" id="chart-id" :chartData="chartData" :chartOptions="chartOptions" />
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import { Bar } from 'vue-chartjs/legacy';
import {
  Chart as ChartJS,
  Title,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

@Component({
  components: {
    Bar,
  },
})

export default class StatsChart extends Vue {
  @Prop({ default: () => [] }) datasets!: number[][];
  @Prop({ default: () => [] }) labels!: string[];
  @Prop({ default: () => [] }) attributes!: string[];
  @Prop({ default: () => [] }) colors!: string[];

  chartData = {};
  chartOptions = {};

  created() {
    let sets = [];
    for (let i = 0; i < this.datasets.length; i++) {

      sets.push({
        label: this.attributes.at(i),
        backgroundColor: this.colors.at(i),
        data: this.datasets.at(i),
      });
    }

    this.chartData = {
      labels: this.labels,
      datasets: sets,
    };

    this.chartOptions = {
      responsive: true,
    };
  }
}
</script>

<style scoped></style>