import { createRouter, createWebHistory } from 'vue-router'
import Login from '../components/LoginPage'
import Main from '@/components/MainPage'
import Home from '@/components/HomePage'
import Department from '@/components/DepartmentPage'
import User from '@/components/UserPage'
import Role from '@/components/RolePage'
import Clazz from '@/components/ClazzPage'
import Analysis from '@/components/AnalysisPage'
import Instructor from '@/components/InstructorPage'
import Leave from '@/components/LeavePage'
import Menu from '@/components/MenuPage'

const routes = [
  {
    path: '/',
    name: 'login',
    component: Login
  },
  {
    path: '/main',
    name: 'main',
    component: Main,
    children: [
      { path: 'home', component: Home },
      {
        path: 'department',
        component: Department
      },
      {
        path: 'user',
        component: User
      },
      {
        path: 'role',
        component: Role
      },
      {
        path: 'clazz',
        component: Clazz
      },
      {
        path: 'analysis',
        component: Analysis
      },
      {
        path: 'instructor',
        component: Instructor
      },
      {
        path: 'leave',
        component: Leave
      },
      {
        path: 'menu',
        component: Menu
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
