import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { TaskDetailComponent } from './components/task-detail/task-detail.component';
import { TaskEditComponent } from './components/task-edit/task-edit.component';
import { MainLayout } from './components/layout/main-layout/main-layout';
import { IsLoggedGuard, IsNotLoggedGuard } from './guards/session.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent, canActivate: [IsNotLoggedGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [IsNotLoggedGuard] },
  {
    path: '',
    component: MainLayout,
    canActivate: [IsLoggedGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'tasks/:id', component: TaskDetailComponent },
      { path: 'tasks/:id/edit', component: TaskEditComponent }
    ]
  },
  { path: '**', redirectTo: '/dashboard' }
];
