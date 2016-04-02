Rails.application.routes.draw do


  root 'static_pages#home'

  resources :lessons
  resources :instructions
  resources :answers


end
