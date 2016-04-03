Rails.application.routes.draw do


 
  root 'static_pages#home'

  resources :lessons
  resources :instructions do
    resources :answers, shallow: true
  end
  get    'dashboard'   => 'lessons#dashboard'

  resources :answer_jumps

  namespace :api do
    resources :lessons
  end


end
