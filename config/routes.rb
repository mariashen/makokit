Rails.application.routes.draw do


 
  root 'static_pages#home'
  get    'dashboard'   => 'lessons#dashboard'

  resources :lessons
  resources :instructions do
    resources :answers, shallow: true
  end

  resources :answers do 
    resources :answer_jumps, shallow: true
  end

  namespace :api do
    resources :lessons
  end


end
