class AddUploadedImageToInstruction < ActiveRecord::Migration
  def change
    add_attachment :instructions, :avatar
  end
end
