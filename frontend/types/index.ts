// Core TypeScript interfaces modeling ForgeAI domain entities across frontend views.

// Represents the authenticated user profile and gamification progression.
export interface User {
  id: number;
  name: string;
  email: string;
  githubUsername: string;
  profileImage?: string;
  bio?: string;
  xp: number;
  level: number;
  streak: number;
  role: string;
  isDemo?: boolean;
}

// Authentication token payload returned from login/register endpoints.
export interface AuthResponse {
  token: string;
  tokenType: string;
  id: number;
  name: string;
  email: string;
  githubUsername: string;
  xp: number;
  level: number;
  streak: number;
  role: string;
  isDemo: boolean;
}

// Composite deterministic Engineering Score and Industry Readiness metrics.
export interface EngineeringScore {
  overallScore: number;
  industryReadinessScore: number;
  programming: number;
  problemSolving: number;
  projects: number;
  github: number;
  debugging: number;
  testing: number;
  systemDesign: number;
  security: number;
  communication: number;
  consistency: number;
  technicalReadiness: number;
  projectReadiness: number;
  interviewReadiness: number;
  engineeringPracticeReadiness: number;
  currentFocus: string;
  focusReason: string;
  isDemoData: boolean;
}

// 6 Core Engineer DNA dimensions for radar chart visualization.
export interface EngineerDnaDimension {
  dimension: string;
  score: number;
  fullMark: number;
}

// Synced GitHub repository with code health indicators.
export interface Repository {
  id: number;
  name: string;
  fullName: string;
  description: string;
  language: string;
  starsCount: number;
  forksCount: number;
  htmlUrl: string;
  healthScore: number;
  detectedTechnologies: string;
  strengthsJson: string;
  improvementsJson: string;
}

// Technical assessment with multiple-choice and conceptual questions.
export interface Assessment {
  id: number;
  title: string;
  category: string;
  difficulty: string;
  durationMinutes: number;
  totalQuestions: number;
  description: string;
  questions?: Question[];
}

// Individual question belonging to an assessment.
export interface Question {
  id: number;
  questionText: string;
  category: string;
  difficulty: string;
  optionsJson: string;
  correctOptionIndex?: number;
  explanation?: string;
}

// Evaluated assessment result summary.
export interface AssessmentResult {
  id: number;
  assessmentId: number;
  assessmentTitle: string;
  category: string;
  scorePercentage: number;
  correctCount: number;
  totalQuestions: number;
  performanceTier: 'Strong' | 'Average' | 'Needs Improvement';
  xpAwarded: number;
}

// Identified skill gap between current proficiency and industry benchmark.
export interface SkillGap {
  id: number;
  skillName: string;
  category: string;
  currentLevel: string;
  targetLevel: string;
  gapSeverity: 'HIGH' | 'MEDIUM' | 'LOW';
  recommendedAction: string;
}

// Personalized phased learning path.
export interface Roadmap {
  id: number;
  title: string;
  targetRole: string;
  totalPhases: number;
  overallProgress: number;
  items: RoadmapItem[];
}

// Milestone item within an engineering roadmap phase.
export interface RoadmapItem {
  id: number;
  phaseNumber: number;
  phaseTitle: string;
  skillName: string;
  currentLevel: string;
  targetLevel: string;
  recommendedResources: string;
  estimatedHours: number;
  status: 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED';
  progressPercentage: number;
}

// Practical hands-on engineering assignment.
export interface Mission {
  id: number;
  title: string;
  description: string;
  skillCategory: string;
  difficulty: string;
  xpReward: number;
  requirements: string;
  completionCriteria: string;
  submission?: {
    status: string;
    repositoryUrl: string;
    submissionNotes: string;
  };
}

// Interactive buggy code puzzle designed to teach root-cause analysis.
export interface DebuggingChallenge {
  id: number;
  title: string;
  description: string;
  language: string;
  difficulty: string;
  buggyCode: string;
  solutionCode?: string;
  bugExplanation?: string;
  hintsJson: string;
  testCaseDescription: string;
  xpReward: number;
}

// Structured feedback returned by the AI code review engine.
export interface CodeReviewFeedback {
  qualityScore: number;
  summary: string;
  strengths: string[];
  issues: string[];
  securityConcerns: string[];
  performanceSuggestions: string[];
  refactoringSuggestions: string[];
  refactoredCode: string;
  isMockAi: boolean;
}

// Technical interview simulation session.
export interface Interview {
  id: number;
  role: string;
  difficulty: string;
  category: string;
  overallScore?: number;
  technicalScore?: number;
  communicationScore?: number;
  problemSolvingScore?: number;
  summary?: string;
  status: string;
  questions: InterviewQuestion[];
}

// Individual interview question.
export interface InterviewQuestion {
  id: number;
  questionText: string;
  category: string;
  idealKeyPoints: string;
}

// Final interview evaluation scorecard.
export interface InterviewEvaluation {
  interviewId: number;
  overallScore: number;
  technicalScore: number;
  communicationScore: number;
  problemSolvingScore: number;
  summary: string;
  strengths: string[];
  weaknesses: string[];
  improvementSuggestions: string[];
  readinessVerdict: string;
}
