class AddDescriptionToLessons < ActiveRecord::Migration
  def change
    add_column :lessons, :category, :string
    add_column :lessons, :description, :text
  end
end
