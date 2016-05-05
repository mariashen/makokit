Rails.application.routes.draw do


 
  root 'static_pages#home'
  get    'dashboard'   => 'lessons#dashboard'
  get '/webhook', to: 'webhook#permission'
  post '/webhook', to: 'webhook#receive'
  get '/test', to: 'test#test'

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
